package org.broadleafcommerce.payment.service.gateway;

import com.alipay.api.AlipayApiException;
import com.alipay.api.DefaultAlipayClient;
import com.alipay.api.domain.AlipayTradeWapPayModel;
import com.alipay.api.request.AlipayTradeWapPayRequest;
import com.alipay.api.response.AlipayTradeWapPayResponse;
import org.broadleafcommerce.common.payment.PaymentType;
import org.broadleafcommerce.common.payment.domain.PaymentUnifiedOrder;
import org.broadleafcommerce.common.payment.domain.PaymentUnifiedOrderImpl;
import org.broadleafcommerce.common.payment.dto.PaymentRequestDTO;
import org.broadleafcommerce.common.payment.dto.PaymentResponseDTO;
import org.broadleafcommerce.common.payment.service.AbstractPaymentGatewayHostedService;
import org.broadleafcommerce.common.payment.service.PaymentGatewayHostedService;
import org.broadleafcommerce.common.payment.service.PaymentUnifiedOrderService;
import org.broadleafcommerce.common.vendor.service.exception.PaymentException;
import org.broadleafcommerce.vendor.alipay.service.payment.AlipayPaymentGatewayType;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.util.Date;

@Service("blAlipayWapHostedService")
public class AlipayWapHostedServiceImpl extends AbstractPaymentGatewayHostedService implements PaymentGatewayHostedService {

    @Resource(name = "blAlipayConfiguration")
    private AlipayConfiguration alipayConfiguration;

    @Resource(name = "blAlipayClient")
    private DefaultAlipayClient alipayClient;

    @Resource(name = "zjcmsPaymentUnifiedOrderService")
    private PaymentUnifiedOrderService paymentUnifiedOrderService;

    @Override
    public PaymentResponseDTO requestHostedEndpoint(PaymentRequestDTO paymentRequestDTO) throws PaymentException {
        PaymentUnifiedOrder unifiedOrder = new PaymentUnifiedOrderImpl();
        unifiedOrder.setAmount(new BigDecimal(Double.valueOf(paymentRequestDTO.getTransactionTotal())));
        unifiedOrder.setOrderId(Long.valueOf(paymentRequestDTO.getOrderId()));
        unifiedOrder.setPaymentType("Alipay");
        //unifiedOrder.setResponse();
        Date now = new Date();
        unifiedOrder.setSubmitedDate(now);
        //unifiedOrder.setTimeExpire(calendar.getTime());
        PaymentUnifiedOrder responseUnifiedOrder = paymentUnifiedOrderService.create(unifiedOrder);

        PaymentResponseDTO paymentResponseDTO = new PaymentResponseDTO(PaymentType.THIRD_PARTY_ACCOUNT, AlipayPaymentGatewayType.ALIPAY);
        // ?????????????????????????????????????????????????????????????????????
        String out_trade_no = responseUnifiedOrder.getId().toString(); //paymentRequestDTO.getOrderId();
        // ?????????????????????
        String subject = "??????????????????-" + out_trade_no;
        //System.out.println(subject);
        // ?????????????????????
        String total_amount = paymentRequestDTO.getTransactionTotal();
        // ?????????????????????
        String body = paymentRequestDTO.getOrderDescription();
        // ???????????? ??????
        String timeout_express="30m";
        // ??????????????? ??????
        String product_code = "QUICK_WAP_WAY";
        /**********************/
        // SDK ??????????????????????????????????????????????????????????????????????????????????????????????????????????????????
        //??????RSA????????????
        //AlipayClient client = new DefaultAlipayClient(AlipayConfig.URL, AlipayConfig.APPID, AlipayConfig.RSA_PRIVATE_KEY, AlipayConfig.FORMAT, AlipayConfig.CHARSET, AlipayConfig.ALIPAY_PUBLIC_KEY,AlipayConfig.SIGNTYPE);
        AlipayTradeWapPayRequest alipay_request = new AlipayTradeWapPayRequest();

        // ????????????????????????
        AlipayTradeWapPayModel model=new AlipayTradeWapPayModel();
        model.setOutTradeNo(out_trade_no);
        model.setSubject(subject);
        model.setTotalAmount(total_amount);
        model.setBody(body);
        model.setTimeoutExpress(timeout_express);
        model.setProductCode(product_code);
        alipay_request.setBizModel(model);

        // ????????????????????????
        alipay_request.setNotifyUrl(alipayConfiguration.getNotifyUrl());
        // ??????????????????
        alipay_request.setReturnUrl(alipayConfiguration.getReturnUrl() + "/" + paymentRequestDTO.getOrderId());

        // form????????????
        String form = "";
        try {
            // ??????SDK????????????
            AlipayTradeWapPayResponse response = alipayClient.pageExecute(alipay_request);
            if (response.isSuccess()){
                paymentResponseDTO.responseMap("form",response.getBody());
                paymentResponseDTO.successful(true);
                paymentResponseDTO.valid(true);
            }else {
                paymentResponseDTO.successful(false);
                paymentResponseDTO.valid(false);
            }
            /*response.setContentType("text/html;charset=" + AlipayConfig.CHARSET);
            response.getWriter().write(form);//????????????????????????html???????????????
            response.getWriter().flush();
            response.getWriter().close();*/
        } catch (AlipayApiException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
            paymentResponseDTO.successful(false);
            paymentResponseDTO.valid(false);
        }
        return paymentResponseDTO;
    }
}
