/*
 * Copyright 2008-2012 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *       http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package cn.com.kelaikewang.controller.account;

import cn.com.kelaikewang.commons.zxing.ZxingUtils;
import cn.com.kelaikewang.core.logistics.service.ShippingService;
import cn.com.kelaikewang.core.logistics.service.type.ConfirmReceiptType;
import cn.com.kelaikewang.core.order.domain.ZaoJiCMSOrder;
import cn.com.kelaikewang.core.order.dto.CreateAfterSalesDTO;
import cn.com.kelaikewang.core.order.service.AfterSalesService;
import cn.com.kelaikewang.core.order.service.type.OrderListStatus;
import cn.com.kelaikewang.core.rating.dto.OrderItemRatingDTO;
import cn.com.kelaikewang.core.rating.dto.OrderRatingDTO;
import cn.com.kelaikewang.core.rating.service.ZaoJiCMSRatingService;
import cn.com.kelaikewang.infrastructure.dto.PageOfItems;
import cn.com.kelaikewang.infrastructure.dto.ResponseDTO;
import cn.com.kelaikewang.core.order.service.ZaoJiCMSOrderService;
import org.apache.commons.lang.StringUtils;
import org.broadleafcommerce.core.web.controller.account.BroadleafOrderHistoryController;
import org.broadleafcommerce.profile.core.service.CountryService;
import org.broadleafcommerce.profile.web.core.CustomerState;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.OutputStream;

@Controller
@RequestMapping("/account/orders")
public class OrderHistoryController extends BroadleafOrderHistoryController {
    protected static final Logger LOGGER = LoggerFactory.getLogger(OrderHistoryController.class);

    @Resource(name = "blRatingService")
    protected ZaoJiCMSRatingService ratingService;

    @Resource(name = "blCountryService")
    protected CountryService countryService;

    @Resource(name = "blOrderService")
    protected ZaoJiCMSOrderService nextShopOrderService;


    @Resource(name = "zjcmsShippingService")
    protected ShippingService shippingService;

    @Resource(name = "zjcmsAfterSalesService")
    protected AfterSalesService afterSalesService;

    @RequestMapping(method = RequestMethod.GET)
    public String viewOrderHistory(HttpServletRequest request, Model model) {
        /*return super.viewOrderHistory(request, model); */
        String pageIndexParam = request.getParameter("pageIndex");
        int pageIndex;
        if (StringUtils.isEmpty(pageIndexParam)){
            pageIndex = 1;
        }else {
            pageIndex = Integer.parseInt(pageIndexParam);
        }
        OrderListStatus orderListStatus = OrderListStatus.getInstance(request.getParameter("status"));
        PageOfItems<ZaoJiCMSOrder> pageOfItems = nextShopOrderService.findOrderPageForCustomer(CustomerState.getCustomer(),orderListStatus,pageIndex,10);
        model.addAttribute("orders",pageOfItems.getItems());
        model.addAttribute("page",pageOfItems);
        return this.getOrderHistoryView();
    }

    @Override
    public String getOrderHistoryView() {
        return "account/order/orderHistory";
    }

    @RequestMapping(value = "/{orderNumber}", method = RequestMethod.GET)
    public String viewOrderDetails(HttpServletRequest request, Model model, @PathVariable("orderNumber") String orderNumber) {
        return super.viewOrderDetails(request, model, orderNumber);
    }
    @RequestMapping(value = "/qrcode/{orderId}", method = RequestMethod.GET)
    public void viewOrderQRCode(HttpServletRequest request, HttpServletResponse response, Model model, @PathVariable("orderId") long orderId) throws Exception {
        response.setContentType("image/png");
        ZaoJiCMSOrder order = (ZaoJiCMSOrder)orderService.findOrderById(orderId);
        byte[] pngBytes = ZxingUtils.generateQRCodeImage(order.getWriteOffCode().toString(),400,400);
        OutputStream outputStream = response.getOutputStream();
        outputStream.write(pngBytes,0,pngBytes.length);
        outputStream.close();
    }
    @RequestMapping(value = "/barcode/{orderId}", method = RequestMethod.GET)
    public void viewOrderBarcode(HttpServletRequest request, HttpServletResponse response, Model model, @PathVariable("orderId") long orderId) throws Exception {
        response.setContentType("image/png");
        ZaoJiCMSOrder order = (ZaoJiCMSOrder)orderService.findOrderById(orderId);
        byte[] pngBytes = ZxingUtils.generateBarCodeImage(order.getWriteOffCode().toString(),400,100);
        OutputStream outputStream = response.getOutputStream();
        outputStream.write(pngBytes,0,pngBytes.length);
        outputStream.close();
    }
    @RequestMapping(value = "/writeOffCode/{orderId}", method = RequestMethod.GET)
    @ResponseBody
    public String getQRCodeId(HttpServletRequest request, HttpServletResponse response, Model model, @PathVariable("orderId") long orderId) throws Exception {
        ZaoJiCMSOrder order = (ZaoJiCMSOrder)orderService.findOrderById(orderId);
        String qrcodeId = order.getWriteOffCode().toString();
        StringBuilder stringBuilder = new StringBuilder();
        for (int i=1;i<=qrcodeId.length();i++){
            if (i % 4 == 0){
                stringBuilder.append(qrcodeId.charAt(i-1));
                stringBuilder.append("&nbsp;&nbsp;&nbsp;&nbsp;");
            }else {
                stringBuilder.append(qrcodeId.charAt(i-1));
            }
        }
        return stringBuilder.toString();
    }
    @RequestMapping(value = "confirmReceipt",method = RequestMethod.POST)
    @ResponseBody
    public ResponseDTO processConfirmReceipt(@RequestParam("orderId")long orderId){
        shippingService.confirmReceipt(orderId, ConfirmReceiptType.MANUAL);
        return ResponseDTO.success("确实收货成功");
    }
    @RequestMapping(value = "orderDetails",method = RequestMethod.GET)
    public String viewOrderDetails(@RequestParam("orderId")long orderId,Model model) throws Exception {
        ZaoJiCMSOrder order = (ZaoJiCMSOrder)orderService.findOrderById(orderId);
        model.addAttribute("order",order);
        if(!org.apache.commons.lang3.StringUtils.isEmpty(order.getLogisticsTrackingNumber())) {
            try {
                model.addAttribute("logisticTrackingInfo", shippingService.getLogisticTrackingInfo(orderId));
            } catch (Exception e) {
                e.printStackTrace();
                LOGGER.error("获取订单物流信息异常，订单id=" + orderId, e);
            }
        }
        return "account/order/orderDetails";
    }
    @RequestMapping(value = "orderRefund",method = RequestMethod.GET)
    public String viewOrderRefund(@RequestParam("orderId")long orderId,Model model){
        ZaoJiCMSOrder order = (ZaoJiCMSOrder)orderService.findOrderById(orderId);
        model.addAttribute("order",order);
        return "account/order/orderRefund";
    }
    @RequestMapping(value = "orderRefund",method = RequestMethod.POST)
    @ResponseBody
    public ResponseDTO processOrderRefund(@RequestBody CreateAfterSalesDTO afterSalesDTO){
        afterSalesDTO.setCustomerId(CustomerState.getCustomer().getId());
        afterSalesService.create(afterSalesDTO);
        return ResponseDTO.success("退款申请提交成功");
    }
    @RequestMapping(value = "orderRefundDetails",method = RequestMethod.GET)
    public String viewOrderRefundDetails(@RequestParam("orderId")long orderId,Model model){
        ZaoJiCMSOrder order = (ZaoJiCMSOrder)orderService.findOrderById(orderId);
        model.addAttribute("order",order);
        model.addAttribute("customer",order.getCustomer());
        model.addAttribute("afterSales",order.getAfterSales().get(0));
        return "account/order/orderRefundDetails";
    }
    @RequestMapping(value = "orderRate/{orderId}",method = RequestMethod.GET)
    public String viewOrderRate(@PathVariable("orderId")long orderId,Model model){
        ZaoJiCMSOrder order = (ZaoJiCMSOrder)orderService.findOrderById(orderId);
        model.addAttribute("order",order);
        return "account/order/orderRate";
    }
    @RequestMapping(value = "orderRate",method = RequestMethod.POST)
    @ResponseBody
    public ResponseDTO processOrderRate(@RequestBody OrderRatingDTO orderRatingDTO){
        if (orderRatingDTO == null){
            return ResponseDTO.fail("请填写评论");
        }
        for (OrderItemRatingDTO form : orderRatingDTO.getOrderItemRates()){
            if (StringUtils.isEmpty(form.getProductId())){
                return ResponseDTO.fail("商品id无效");
            }

            if (form.getRating() == null || form.getRating() == 0){
                return ResponseDTO.fail("您忘记打分咯!");
            }
            if (org.apache.commons.lang3.StringUtils.isEmpty(form.getReviewText())){
                //return "<span style=\"color: #FF2626;\">请填写品论内容!</span>";
                return ResponseDTO.fail("请填写评论内容!");
            }
        }
        ratingService.submitOrderRating(orderRatingDTO);
        return ResponseDTO.success("评论提交成功");
    }
    @RequestMapping(value = "updateOrderRate/{orderId}",method = RequestMethod.GET)
    public String viewUpdateOrderRate(@PathVariable("orderId")long orderId,Model model){
        ZaoJiCMSOrder order = (ZaoJiCMSOrder)orderService.findOrderById(orderId);
        model.addAttribute("order",order);
        model.addAttribute("orderRating",ratingService.getOrderRating(orderId));
        return "account/order/updateOrderRate";
    }
    @RequestMapping(value = "updateOrderRate",method = RequestMethod.POST)
    @ResponseBody
    public ResponseDTO processUpdateOrderRate(@RequestBody OrderRatingDTO orderRatingDTO){
        if (orderRatingDTO == null){
            return ResponseDTO.fail("请填写评论");
        }
        for (OrderItemRatingDTO form : orderRatingDTO.getOrderItemRates()){
            if (StringUtils.isEmpty(form.getProductId())){
                return ResponseDTO.fail("商品id无效");
            }

            if (form.getRating() == null || form.getRating() == 0){
                return ResponseDTO.fail("您忘记打分咯!");
            }
            if (org.apache.commons.lang3.StringUtils.isEmpty(form.getReviewText())){
                //return "<span style=\"color: #FF2626;\">请填写品论内容!</span>";
                return ResponseDTO.fail("请填写评论内容!");
            }
        }
        ratingService.updateOrderRating(orderRatingDTO);
        return ResponseDTO.success("评论提交成功");
    }
}
