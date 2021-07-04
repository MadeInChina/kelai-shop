package org.broadleafcommerce.payment.service.gateway;


import com.github.binarywang.wxpay.bean.request.WxPayUnifiedOrderRequest;
import com.github.binarywang.wxpay.bean.result.WxPayUnifiedOrderResult;
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
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.math.BigDecimal;
import java.util.Date;

@Service("blWxPayPageHostedService")
public class WxPayPageHostedServiceImpl extends AbstractPaymentGatewayHostedService implements PaymentGatewayHostedService {

    @Resource(name = "blWxPayConfiguration")
    private WxPayConfiguration wxpayConfiguration;

    @Resource(name = "wxPayService")
    private WxPayService wxService;

    @Resource(name = "zjcmsPaymentUnifiedOrderService")
    private PaymentUnifiedOrderService paymentUnifiedOrderService;

    @Override
    public PaymentResponseDTO requestHostedEndpoint(PaymentRequestDTO paymentRequestDTO) throws PaymentException {
        PaymentUnifiedOrder unifiedOrder = new PaymentUnifiedOrderImpl();
        unifiedOrder.setAmount(new BigDecimal(Double.valueOf(paymentRequestDTO.getTransactionTotal())));
        unifiedOrder.setOrderId(Long.valueOf(paymentRequestDTO.getOrderId()));
        unifiedOrder.setPaymentType("WxPay");
        //unifiedOrder.setResponse();
        Date now = new Date();
        unifiedOrder.setSubmitedDate(now);
        //unifiedOrder.setTimeExpire(calendar.getTime());
        PaymentUnifiedOrder response = paymentUnifiedOrderService.create(unifiedOrder);

        ServletRequestAttributes servletRequestAttributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        HttpServletRequest request = servletRequestAttributes.getRequest();
        request.getSession().setAttribute("unifiedOrderId",response.getId());

        PaymentResponseDTO paymentResponseDTO = new PaymentResponseDTO(PaymentType.THIRD_PARTY_ACCOUNT, WxPayPaymentGatewayType.WXPAY);

        WxPayUnifiedOrderRequest wxPayUnifiedOrderRequest = new WxPayUnifiedOrderRequest();
        wxPayUnifiedOrderRequest.setBody("企业服务订单-" + paymentRequestDTO.getOrderId());
        wxPayUnifiedOrderRequest.setNotifyUrl(wxpayConfiguration.getNotifyUrl());
        wxPayUnifiedOrderRequest.setOutTradeNo(response.getId().toString());
        wxPayUnifiedOrderRequest.setFeeType("CNY");
        BigDecimal yuan = BigDecimal.valueOf(Double.valueOf(paymentRequestDTO.getTransactionTotal()));
        wxPayUnifiedOrderRequest.setTotalFee(yuan.multiply(BigDecimal.valueOf(100)).intValue());
        wxPayUnifiedOrderRequest.setProductId(paymentRequestDTO.getOrderId());
        wxPayUnifiedOrderRequest.setSpbillCreateIp(paymentRequestDTO.getAdditionalFields().get("ip").toString());
        wxPayUnifiedOrderRequest.setNonceStr(RandomStringUtils.random(32,true,true));
        wxPayUnifiedOrderRequest.setTradeType("NATIVE");
        wxPayUnifiedOrderRequest.setOpenid((String) paymentRequestDTO.getAdditionalFields().get("wxOpenId"));
        try {
            WxPayUnifiedOrderResult unifiedOrderResult = this.wxService.unifiedOrder(wxPayUnifiedOrderRequest);
            paymentResponseDTO.successful(true);
            paymentResponseDTO.valid(true);

            paymentResponseDTO.responseMap("return_code",unifiedOrderResult.getReturnCode());
            paymentResponseDTO.responseMap("return_msg",unifiedOrderResult.getReturnMsg());
            paymentResponseDTO.responseMap("result_code",unifiedOrderResult.getResultCode());
            paymentResponseDTO.responseMap("err_code",unifiedOrderResult.getErrCode());
            paymentResponseDTO.responseMap("err_code_des",unifiedOrderResult.getErrCodeDes());
            paymentResponseDTO.responseMap("appid",unifiedOrderResult.getAppid());
            paymentResponseDTO.responseMap("mch_id",unifiedOrderResult.getMchId());
            paymentResponseDTO.responseMap("sub_appid",unifiedOrderResult.getSubAppId());
            paymentResponseDTO.responseMap("sub_mch_id",unifiedOrderResult.getSubMchId());
            paymentResponseDTO.responseMap("nonce_str",unifiedOrderResult.getNonceStr());
            paymentResponseDTO.responseMap("sign",unifiedOrderResult.getSign());
            paymentResponseDTO.responseMap("prepay_id",unifiedOrderResult.getPrepayId());
            paymentResponseDTO.responseMap("trade_type",unifiedOrderResult.getTradeType());
            paymentResponseDTO.responseMap("mweb_url",unifiedOrderResult.getMwebUrl());
            paymentResponseDTO.responseMap("code_url",unifiedOrderResult.getCodeURL());


        } catch (WxPayException e) {
            e.printStackTrace();
            paymentResponseDTO.successful(false);
            paymentResponseDTO.valid(false);
        }
        return paymentResponseDTO;

    }
}
