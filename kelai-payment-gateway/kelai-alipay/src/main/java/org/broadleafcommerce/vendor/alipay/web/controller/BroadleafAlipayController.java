package org.broadleafcommerce.vendor.alipay.web.controller;

import com.alipay.api.AlipayApiException;
import com.alipay.api.internal.util.AlipaySignature;
import cn.com.kelaikewang.commons.web.RequestUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.broadleafcommerce.common.payment.domain.PaymentUnifiedOrder;
import org.broadleafcommerce.common.payment.dto.PaymentRequestDTO;
import org.broadleafcommerce.common.payment.dto.PaymentResponseDTO;
import org.broadleafcommerce.common.payment.service.*;
import org.broadleafcommerce.common.vendor.service.exception.PaymentException;
import org.broadleafcommerce.common.web.payment.controller.ZaoJiCMSPaymentGatewayAbstractController;
import org.broadleafcommerce.core.order.domain.Order;
import org.broadleafcommerce.core.order.service.OrderService;
import org.broadleafcommerce.core.order.service.type.OrderStatus;
import org.broadleafcommerce.payment.service.gateway.AlipayConfiguration;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

@Controller("blAlipayController")
@RequestMapping("/" + BroadleafAlipayController.GATEWAY_CONTEXT_KEY)
public class BroadleafAlipayController extends ZaoJiCMSPaymentGatewayAbstractController {

    protected static final Log LOG = LogFactory.getLog(BroadleafAlipayController.class);

    public static final String GATEWAY_CONTEXT_KEY = "alipay";

    @Resource(name = "blAlipayWebResponseService")
    protected PaymentGatewayWebResponseService paymentGatewayWebResponseService;

    @Resource(name = "blAlipayConfiguration")
    protected AlipayConfiguration alipayConfiguration;

    @Autowired(required=false)
    protected CurrentOrderPaymentRequestService currentOrderPaymentRequestService;

    @Resource(name = "blAlipayPageHostedService")
    protected PaymentGatewayHostedService paymentGatewayPageHostedService;

    @Resource(name = "blAlipayWapHostedService")
    protected PaymentGatewayHostedService paymentGatewayWapHostedService;

    @Resource(name = "blAlipayReportingService")
    protected PaymentGatewayReportingService reportingService;

    @Resource(name = "blOrderService")
    protected OrderService orderService;

    @Resource(name = "zjcmsPaymentUnifiedOrderService")
    protected PaymentUnifiedOrderService paymentUnifiedOrderService;

    @Resource(name = "blAlipayReportingService")
    protected PaymentGatewayReportingService paymentGatewayReportingService;


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
        return alipayConfiguration;
    }
    @RequestMapping(value = "/unifiedOrder")
    @ResponseBody
    public void unifiedOrder(HttpServletRequest request, HttpServletResponse response,Model model) throws PaymentException, IOException {
        PaymentRequestDTO paymentRequestDTO = getCurrentOrder();

        PaymentResponseDTO paymentResponseDTO;
        if (RequestUtils.isMobileOrTabletDevice()){
            paymentResponseDTO = paymentGatewayWapHostedService.requestHostedEndpoint(paymentRequestDTO);
        }else {
            paymentResponseDTO = paymentGatewayPageHostedService.requestHostedEndpoint(paymentRequestDTO);
        }
        response.setContentType("text/html;charset=utf-8");
        PrintWriter printWriter = response.getWriter();
        printWriter.write("<html><head><title>???????????????</title></head><body>");

        if (paymentResponseDTO.isSuccessful()){
            Long addressId = (Long) paymentRequestDTO.getAdditionalFields().get("ADDRESS_ID");
          /*  if(addressId == null){
                response.sendRedirect(request.getHeader("Referer"));
                return;
            }*/
          //????????????????????????????????????????????????????????????????????????
          if (addressId != null) {
              saveOrderShippingAddress(Long.valueOf(paymentRequestDTO.getOrderId()), addressId);
          }
            printWriter.write(paymentResponseDTO.getResponseMap().get("form"));
        }else {
            printWriter.write("<div>?????????????????????</div>");
        }
        printWriter.write("</body></html>");
        printWriter.close();
    }
    @Override
    @RequestMapping(value = "/return/{orderId}")
    public String returnEndpoint(Model model,
                                 HttpServletRequest httpServletRequest,
                                 RedirectAttributes redirectAttributes,
                                 @PathVariable Map<String, String> map) {
        String view = null;
        try {
            view = this.processReturn(model, httpServletRequest, redirectAttributes);
        }catch (Exception e){
            LOG.error("??????????????????",e);
            view = this.getErrorViewRedirect();
        }
        //???????????????GET??????????????????
        try {
            Map<String, String> params = new HashMap<String, String>();
            Map requestParams = httpServletRequest.getParameterMap();
            for (Iterator iter = requestParams.keySet().iterator(); iter.hasNext(); ) {
                String name = (String) iter.next();
                String[] values = (String[]) requestParams.get(name);
                String valueStr = "";
                for (int i = 0; i < values.length; i++) {
                    valueStr = (i == values.length - 1) ? valueStr + values[i]
                            : valueStr + values[i] + ",";
                }
                //????????????????????????????????????????????????????????????mysign???sign??????????????????????????????????????????
                valueStr = new String(valueStr.getBytes("ISO-8859-1"), "utf-8");
                params.put(name, valueStr);
            }

            //???????????????????????????????????????????????????????????????????????????????????????????????????(??????????????????)//
            //???????????????

            //String out_trade_no = new String(httpServletRequest.getParameter("out_trade_no").getBytes("ISO-8859-1"), "UTF-8");

            //??????????????????

            //String trade_no = new String(httpServletRequest.getParameter("trade_no").getBytes("ISO-8859-1"), "UTF-8");

            //???????????????????????????????????????????????????????????????????????????????????????????????????(??????????????????)//
            //??????????????????????????????
            //boolean AlipaySignature.rsaCheckV1(Map<String, String> params, String publicKey, String charset, String sign_type)
            boolean verify_result = AlipaySignature.rsaCheckV1(params, alipayConfiguration.getAlipayPublicKey(), "UTF-8", "RSA2");

            if (verify_result) {//????????????
                return view;
                //////////////////////////////////////////////////////////////////////////////////////////
            } else {
                return this.getErrorViewRedirect();
            }
        } catch (AlipayApiException | UnsupportedEncodingException e) {
            e.printStackTrace();
            return this.getErrorViewRedirect();
        }
    }
    private String processReturn(Model model, HttpServletRequest request,
                                 final RedirectAttributes redirectAttributes) throws PaymentException {
        PaymentRequestDTO paymentRequestDTO = new PaymentRequestDTO();
        paymentRequestDTO.additionalField("unifiedOrderId",request.getParameter("out_trade_no"));
        PaymentResponseDTO responseDTO = paymentGatewayReportingService.findDetailsByTransaction(paymentRequestDTO);
        return processApplyPaymentToOrder(model,request,redirectAttributes,responseDTO);
    }

    @RequestMapping(value = "/notify")
    public void notify(Model model, HttpServletRequest httpServletRequest, RedirectAttributes redirectAttributes, HttpServletResponse response) throws PaymentException, IOException {

        LOG.info("????????????alipay????????????");
        String out_trade_no = new String(httpServletRequest.getParameter("out_trade_no").getBytes("ISO-8859-1"), "UTF-8");
        PaymentUnifiedOrder paymentUnifiedOrder = paymentUnifiedOrderService.getById(Long.parseLong(out_trade_no));
        if (paymentUnifiedOrder == null){
            responseNotify("error",response);
            LOG.info("????????????UnifiedOrderId=" + out_trade_no);
            return;
        }
        Order order = orderService.findOrderById(paymentUnifiedOrder.getOrderId());
        if (order == null){
            responseNotify("error",response);
            LOG.info("????????????orderId=" + paymentUnifiedOrder.getOrderId());
            return;
        }
        if (OrderStatus.SUBMITTED.getType().equals(order.getStatus().getType())){
            responseNotify("success",response);
            return;
        }

        super.process(model, httpServletRequest, redirectAttributes);
        response.setCharacterEncoding("UTF-8");
        responseNotify("success",response);

    }
    private void responseNotify(String content,HttpServletResponse response) throws IOException {
        PrintWriter printWriter = response.getWriter();
        printWriter.write(content);
        printWriter.close();
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

    protected PaymentRequestDTO getCurrentOrder() {
        if (LOG.isErrorEnabled()) {
            if (currentOrderPaymentRequestService == null) {
                LOG.trace("getCurrentOrder: CurrentOrderPaymentRequestService is null. Please check your configuration.");
            }
        }

        if (currentOrderPaymentRequestService != null) {
            return currentOrderPaymentRequestService.getPaymentRequestFromCurrentOrder();
        }
        return null;
    }

}
