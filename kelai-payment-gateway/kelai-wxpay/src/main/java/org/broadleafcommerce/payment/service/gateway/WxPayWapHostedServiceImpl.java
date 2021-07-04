package org.broadleafcommerce.payment.service.gateway;


import com.github.binarywang.wxpay.bean.request.WxPayUnifiedOrderRequest;
import com.github.binarywang.wxpay.exception.WxPayException;
import com.github.binarywang.wxpay.service.WxPayService;
import org.apache.commons.lang3.RandomStringUtils;
import org.broadleafcommerce.common.payment.PaymentType;
import org.broadleafcommerce.common.payment.domain.PaymentUnifiedOrder;
import org.broadleafcommerce.common.payment.domain.PaymentUnifiedOrderImpl;
import org.broadleafcommerce.common.payment.dto.PaymentRequestDTO;
import org.broadleafcommerce.common.payment.dto.PaymentResponseDTO;
import org.broadleafcommerce.common.payment.service.AbstractPaymentGatewayHostedService;
import org.broadleafcommerce.common.payment.service.PaymentGatewayHostedService;
import org.broadleafcommerce.common.payment.service.PaymentUnifiedOrderService;
import org.broadleafcommerce.common.vendor.service.exception.PaymentException;
import org.broadleafcommerce.vendor.wxpay.service.payment.WxPayPaymentGatewayType;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.util.Date;
import java.util.Map;

@Service("blWxPayWapHostedService")
public class WxPayWapHostedServiceImpl extends AbstractPaymentGatewayHostedService implements PaymentGatewayHostedService {

    @Resource(name = "blWxPayConfiguration")
    private WxPayConfiguration wxpayConfiguration;

    @Resource(name = "wxPayService")
    private WxPayService wxService;

    @Resource(name = "zjcmsPaymentUnifiedOrderService")
    private PaymentUnifiedOrderService paymentUnifiedOrderService;

    @Override
    public PaymentResponseDTO requestHostedEndpoint(PaymentRequestDTO paymentRequestDTO) throws PaymentException {

        PaymentUnifiedOrder unifiedOrder = new PaymentUnifiedOrderImpl();
        unifiedOrder.setAmount(BigDecimal.valueOf(Double.parseDouble(paymentRequestDTO.getTransactionTotal())));
        unifiedOrder.setOrderId(Long.valueOf(paymentRequestDTO.getOrderId()));
        unifiedOrder.setPaymentType("WxPay");
        //unifiedOrder.setResponse();
        Date now = new Date();
        unifiedOrder.setSubmitedDate(now);
        //unifiedOrder.setTimeExpire(calendar.getTime());
        PaymentUnifiedOrder response = paymentUnifiedOrderService.create(unifiedOrder);

        PaymentResponseDTO paymentResponseDTO = new PaymentResponseDTO(PaymentType.THIRD_PARTY_ACCOUNT, WxPayPaymentGatewayType.WXPAY);

        WxPayUnifiedOrderRequest wxPayUnifiedOrderRequest = new WxPayUnifiedOrderRequest();
        wxPayUnifiedOrderRequest.setSignType("MD5");
        wxPayUnifiedOrderRequest.setBody("企业服务订单-" + paymentRequestDTO.getOrderId());
        wxPayUnifiedOrderRequest.setNotifyUrl(wxpayConfiguration.getNotifyUrl());
        wxPayUnifiedOrderRequest.setOutTradeNo(response.getId().toString());
        wxPayUnifiedOrderRequest.setFeeType("CNY");
        BigDecimal yuan = BigDecimal.valueOf(Double.parseDouble(paymentRequestDTO.getTransactionTotal()));
        wxPayUnifiedOrderRequest.setTotalFee(yuan.multiply(BigDecimal.valueOf(100)).intValue());
        wxPayUnifiedOrderRequest.setProductId(paymentRequestDTO.getOrderId());
        wxPayUnifiedOrderRequest.setSpbillCreateIp(paymentRequestDTO.getAdditionalFields().get("ip").toString());
        wxPayUnifiedOrderRequest.setNonceStr(RandomStringUtils.random(32,true,true));
        wxPayUnifiedOrderRequest.setTradeType("JSAPI");
        wxPayUnifiedOrderRequest.setOpenid((String) paymentRequestDTO.getAdditionalFields().get("wxOpenId"));
        try {
            Map<String, String> payInfo = this.wxService.getPayInfo(wxPayUnifiedOrderRequest);
            paymentResponseDTO.successful(true);
            paymentResponseDTO.valid(true);

            paymentResponseDTO.responseMap("appId",payInfo.get("appId"));
            paymentResponseDTO.responseMap("nonceStr",payInfo.get("nonceStr"));
            paymentResponseDTO.responseMap("package",payInfo.get("package"));
            paymentResponseDTO.responseMap("paySign",payInfo.get("paySign"));
            paymentResponseDTO.responseMap("timeStamp",payInfo.get("timeStamp"));
            paymentResponseDTO.responseMap("signType",payInfo.get("signType"));


        } catch (WxPayException e) {
            e.printStackTrace();
            paymentResponseDTO.successful(false);
            paymentResponseDTO.valid(false);
        }
        return paymentResponseDTO;
    }
}
