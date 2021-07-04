package org.broadleafcommerce.vendor.wxpay.web.controller;

import com.github.binarywang.wxpay.bean.notify.WxPayRefundNotifyResult;
import cn.com.kelaikewang.core.order.service.AfterSalesService;
import cn.com.kelaikewang.core.order.service.ZaoJiCMSOrderService;
import com.github.binarywang.utils.qrcode.QrcodeUtils;
import com.github.binarywang.wxpay.bean.notify.WxPayNotifyResponse;
import com.github.binarywang.wxpay.bean.notify.WxScanPayNotifyResult;
import com.github.binarywang.wxpay.exception.WxPayException;
import com.github.binarywang.wxpay.service.WxPayService;
import cn.com.kelaikewang.commons.web.RequestUtils;
import cn.com.kelaikewang.core.order.service.type.RefundStatus;
import cn.com.kelaikewang.integration.wechat.constant.WeChatConstants;
import me.chanjar.weixin.mp.bean.result.WxMpUser;
import org.broadleafcommerce.common.payment.domain.PaymentUnifiedOrder;
import org.broadleafcommerce.common.payment.dto.PaymentRequestDTO;
import org.broadleafcommerce.common.payment.dto.PaymentResponseDTO;
import org.broadleafcommerce.common.payment.service.*;
import org.broadleafcommerce.common.vendor.service.exception.PaymentException;
import org.broadleafcommerce.common.web.payment.controller.ZaoJiCMSPaymentGatewayAbstractController;
import org.broadleafcommerce.core.order.domain.Order;
import org.broadleafcommerce.payment.service.gateway.WxPayConfiguration;
import org.broadleafcommerce.vendor.wxpay.service.payment.CheckOrderStatusResponse;
import org.broadleafcommerce.vendor.wxpay.service.payment.UnifiedOrderResponse;
import org.broadleafcommerce.vendor.wxpay.service.payment.UnifiedOrderStatusConstant;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.Map;

@Controller("blWxPayController")
@RequestMapping("/" + BroadleafWxPayController.GATEWAY_CONTEXT_KEY)
public class BroadleafWxPayController extends ZaoJiCMSPaymentGatewayAbstractController {

    protected static final Logger LOG = LoggerFactory.getLogger(BroadleafWxPayController.class);

    public static final String GATEWAY_CONTEXT_KEY = "wxpay";

    @Resource(name = "blWxPayWebResponseService")
    protected PaymentGatewayWebResponseService paymentGatewayWebResponseService;

    @Resource(name = "blWxPayReportingService")
    protected PaymentGatewayReportingService paymentGatewayReportingService;

    @Resource(name = "blWxPayConfiguration")
    protected WxPayConfiguration wxpayConfiguration;

    @Autowired(required=false)
    protected CurrentOrderPaymentRequestService currentOrderPaymentRequestService;

    @Resource(name = "blWxPayPageHostedService")
    protected PaymentGatewayHostedService paymentGatewayPageHostedService;

    @Resource(name = "blWxPayWapHostedService")
    protected PaymentGatewayHostedService paymentGatewayWapHostedService;

    @Resource(name = "blWxPayReportingService")
    protected PaymentGatewayReportingService reportingService;

    @Resource(name = "wxPayService")
    protected WxPayService wxPayService;


    @Resource(name = "zjcmsPaymentUnifiedOrderService")
    protected PaymentUnifiedOrderService paymentUnifiedOrderService;

    @Resource(name = "blOrderService")
    protected ZaoJiCMSOrderService nextShopOrderService;

    @Resource(name = "zjcmsAfterSalesService")
    protected AfterSalesService afterSalesService;

    @Override
    public void handleProcessingException(Exception e, RedirectAttributes redirectAttributes) throws PaymentException {
        if (LOG.isTraceEnabled()) {
            LOG.trace("A Processing Exception Occurred for " + GATEWAY_CONTEXT_KEY +
                    ". Adding Error to Redirect Attributes.");
        }

        redirectAttributes.addAttribute(PAYMENT_PROCESSING_ERROR, getProcessingErrorMessage());
    }

    @Override
    public void handleUnsuccessfulTransaction(Model model, RedirectAttributes redirectAttributes, PaymentResponseDTO paymentResponseDTO) throws PaymentException {
        if (LOG.isTraceEnabled()) {
            LOG.trace("The Transaction was unsuccessful for " + GATEWAY_CONTEXT_KEY +
                    ". Adding Error to Redirect Attributes.");
        }

        redirectAttributes.addAttribute(PAYMENT_PROCESSING_ERROR, getProcessingErrorMessage());
    }

    @Override
    public String getGatewayContextKey() {
        return GATEWAY_CONTEXT_KEY;
    }

    @Override
    public PaymentGatewayWebResponseService getWebResponseService() {
        return paymentGatewayWebResponseService;
    }

    @Override
    public PaymentGatewayConfiguration getConfiguration() {
        return wxpayConfiguration;
    }
    @RequestMapping(value = "/unifiedOrder",produces = "application/json")
    @ResponseBody
    public UnifiedOrderResponse unifiedOrder(HttpServletRequest request, HttpServletResponse response, Model model) throws PaymentException, IOException {
        PaymentRequestDTO paymentRequestDTO = getCurrentOrder(request);
        PaymentResponseDTO paymentResponseDTO = paymentGatewayWapHostedService.requestHostedEndpoint(paymentRequestDTO);

        UnifiedOrderResponse unifiedOrderResponse = new UnifiedOrderResponse();
        unifiedOrderResponse.setOrderId(paymentRequestDTO.getOrderId());

        if (paymentResponseDTO.isSuccessful()){
            unifiedOrderResponse.setStatus(UnifiedOrderStatusConstant.SUCCESS);
            unifiedOrderResponse.setMessage("微信下单成功");
            unifiedOrderResponse.setData(paymentResponseDTO.getResponseMap());
            //不一定会有地址，比如虚拟商品订单
            Long addressId = (Long) paymentRequestDTO.getAdditionalFields().get("ADDRESS_ID");
            if (addressId != null) {
                saveOrderShippingAddress(Long.parseLong(paymentRequestDTO.getOrderId()), addressId);
            }

        }else {
            unifiedOrderResponse.setStatus(UnifiedOrderStatusConstant.FAIL);
            unifiedOrderResponse.setMessage("微信下单失败");
        }
        return unifiedOrderResponse;
    }
    @Override
    @RequestMapping(value = "/return")
    public String returnEndpoint(Model model,
                                 HttpServletRequest httpServletRequest,
                                 RedirectAttributes redirectAttributes,
                                 @PathVariable Map<String, String> map) throws PaymentException {
        String view = null;
        try {
            view = this.processReturn(model, httpServletRequest, redirectAttributes);
        }catch (Exception e){
            LOG.error("微信支付回调：处理订单异常",e);
            view = this.getErrorViewRedirect();
        }
        return view;

    }
    private String processCheckOrderStatus(Model model, HttpServletRequest request,
                                           final RedirectAttributes redirectAttributes,
                                           PaymentResponseDTO responseDTO) throws PaymentException {
        return processApplyPaymentToOrder(model,request,redirectAttributes,responseDTO);

    }
    private String processReturn(Model model, HttpServletRequest request,
                                 final RedirectAttributes redirectAttributes) throws PaymentException {
        PaymentRequestDTO paymentRequestDTO = new PaymentRequestDTO();
        paymentRequestDTO.additionalField("unifiedOrderId",request.getParameter("out_trade_no"));
        PaymentResponseDTO responseDTO = paymentGatewayReportingService.findDetailsByTransaction(paymentRequestDTO);
        return processApplyPaymentToOrder(model,request,redirectAttributes,responseDTO);
    }

    @RequestMapping(value = "/qrcode",method = RequestMethod.GET)
    public void qrcode(HttpServletRequest request,HttpServletResponse response) throws PaymentException, IOException {

        PaymentResponseDTO responseDTO = paymentGatewayPageHostedService.requestHostedEndpoint(getCurrentOrder(request));
        if (responseDTO.isSuccessful()) {
            String qrcodeUrl = responseDTO.getResponseMap().get("code_url");
            //String logo = this.getClass().getClassLoader().getResource(wxpayConfiguration.getLogoFile()).getFile();
            byte[] qrcodeBytes = QrcodeUtils.createQrcode(qrcodeUrl, 300, null);
            response.setContentType("image/jpeg");
            //PrintWriter printWriter = response.getWriter();
            OutputStream outputStream = response.getOutputStream();
            outputStream.write(qrcodeBytes, 0, qrcodeBytes.length);

            outputStream.flush();
            outputStream.close();

            PaymentRequestDTO paymentRequestDTO = getCurrentOrder(request);
            Long addressId = (Long) paymentRequestDTO.getAdditionalFields().get("ADDRESS_ID");
            if (addressId != null) {
                saveOrderShippingAddress(Long.parseLong(paymentRequestDTO.getOrderId()), addressId);
            }

        }else {
            response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
        }
    }

    @RequestMapping(value = "/checkOrderStatus",method = RequestMethod.GET)
    @ResponseBody
    public CheckOrderStatusResponse checkOrderStatus(HttpServletRequest request,Model model,RedirectAttributes redirectAttributes){
        Long unifiedOrderId = (Long) request.getSession().getAttribute("unifiedOrderId");
        CheckOrderStatusResponse checkOrderStatusResponse = new CheckOrderStatusResponse();
        if (unifiedOrderId == null){
            checkOrderStatusResponse.setMessage("unifiedOrderId is null");
            return checkOrderStatusResponse;
        }else {

            PaymentUnifiedOrder paymentUnifiedOrder = paymentUnifiedOrderService.getById(unifiedOrderId);
            Long orderId = paymentUnifiedOrder.getOrderId();
            Order order = nextShopOrderService.findOrderById(orderId);
            if (order == null){
                checkOrderStatusResponse.setMessage("order is null");
                return checkOrderStatusResponse;
            }else {
                try {
                    PaymentRequestDTO paymentRequestDTO = new PaymentRequestDTO();
                    //paymentRequestDTO.orderId((String) request.getSession().getAttribute("unifiedOrderId"));
                    paymentRequestDTO.additionalField("unifiedOrderId", unifiedOrderId);
                    PaymentResponseDTO paymentResponseDTO = reportingService.findDetailsByTransaction(paymentRequestDTO);
                    if (paymentResponseDTO.isSuccessful() && paymentResponseDTO.isValid()) {
                        this.processCheckOrderStatus(model, request, redirectAttributes,paymentResponseDTO);
                    }
                    LOG.info("微信支付轮询，订单状态为：" + order.getStatus().getType());
                }catch (Exception e){
                    LOG.error("处理订单异常",e);
                }
                checkOrderStatusResponse.setMessage("");
                checkOrderStatusResponse.setOrderStatus(order.getStatus().getType());
                checkOrderStatusResponse.setOrderNumber(order.getOrderNumber());
                checkOrderStatusResponse.setUnifiedOrderId(unifiedOrderId.toString());
                return checkOrderStatusResponse;
            }
        }
    }

    @RequestMapping(value = "/notify")
    public void notify(Model model, HttpServletRequest httpServletRequest, RedirectAttributes redirectAttributes, HttpServletResponse response) throws PaymentException, IOException {
        LOG.info("收到微信订单通知");
        response.setCharacterEncoding("UTF-8");
        PrintWriter printWriter = response.getWriter();
        this.process(model, httpServletRequest, redirectAttributes);
        printWriter.write("<xml>\n" +
                "\n" +
                "  <return_code><![CDATA[SUCCESS]]></return_code>\n" +
                "  <return_msg><![CDATA[OK]]></return_msg>\n" +
                "</xml>");
        LOG.info("微信订单通知处理成功");

        printWriter.close();
    }
    @RequestMapping(value = "/refundNotify")
    public void refundNotify(HttpServletRequest request,HttpServletResponse response) throws IOException, WxPayException {
        InputStream inStream = request.getInputStream();
        ByteArrayOutputStream outSteam = new ByteArrayOutputStream();
        byte[] buffer = new byte[1024];
        int len = 0;
        while ((len = inStream.read(buffer)) != -1) {
            outSteam.write(buffer, 0, len);
        }
        outSteam.close();
        inStream.close();
        String xmlData = new String(outSteam.toByteArray(), StandardCharsets.UTF_8);
        WxPayRefundNotifyResult notifyResult = wxPayService.parseRefundNotifyResult(xmlData);

        if ("SUCCESS".equals(notifyResult.getReturnCode())){
            String out_refund_no = notifyResult.getReqInfo().getOutRefundNo();
            long afterSalesId = Long.parseLong(out_refund_no);
            if ("SUCCESS".equals(notifyResult.getReqInfo().getRefundStatus())){
                LOG.info("微信退款成功:{}",xmlData);
                afterSalesService.updateRefundStatus(afterSalesId, RefundStatus.SUCCESS);
            }else {
                LOG.info("微信退款失败:{}",xmlData);
                afterSalesService.updateRefundStatus(afterSalesId, RefundStatus.FAIL);
            }
        }else {
            LOG.info("微信退款失败:{}",xmlData);
            //afterSalesService.updateRefundStatus(afterSalesId, RefundStatus.FAIL);
        }
        PrintWriter printWriter = response.getWriter();
        printWriter.write("<xml>\n" +
                "  <return_code><![CDATA[SUCCESS]]></return_code>\n" +
                "  <return_msg><![CDATA[OK]]></return_msg>\n" +
                "</xml>");
        printWriter.close();

    }
    @RequestMapping(value = "/scanpayNotify")
    public String parseScanPayNotifyResult(String xmlData) throws WxPayException {
        final WxScanPayNotifyResult result = this.wxPayService.parseScanPayNotifyResult(xmlData);
        // TODO 根据自己业务场景需要构造返回对象
        return WxPayNotifyResponse.success("成功");
    }
    @Override
    @RequestMapping(value = "/error", method = RequestMethod.GET)
    public String errorEndpoint(Model model,
                                HttpServletRequest httpServletRequest,
                                RedirectAttributes redirectAttributes,
                                @PathVariable Map<String, String> map) throws PaymentException {
        redirectAttributes.addAttribute(PAYMENT_PROCESSING_ERROR,
                httpServletRequest.getParameter(PAYMENT_PROCESSING_ERROR));
        return getCartViewRedirect();
    }
    protected PaymentRequestDTO getCurrentOrder(HttpServletRequest request) {
        if (LOG.isErrorEnabled()) {
            if (currentOrderPaymentRequestService == null) {
                LOG.trace("getCurrentOrder: CurrentOrderPaymentRequestService is null. Please check your configuration.");
            }
        }

        if (currentOrderPaymentRequestService != null) {
            PaymentRequestDTO requestDTO = currentOrderPaymentRequestService.getPaymentRequestFromCurrentOrder();
            WxMpUser wxMpUser = (WxMpUser)request.getSession().getAttribute(WeChatConstants.WX_MP_USER);
            if (wxMpUser != null) {
                requestDTO.additionalField("wxOpenId", wxMpUser.getOpenId());
            }
            requestDTO.additionalField("ip", RequestUtils.getIpAddr(request));
            return requestDTO;
        }
        return null;
    }
}
