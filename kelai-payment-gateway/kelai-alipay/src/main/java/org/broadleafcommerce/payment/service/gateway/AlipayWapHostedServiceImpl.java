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
        // 商户订单号，商户网站订单系统中唯一订单号，必填
        String out_trade_no = responseUnifiedOrder.getId().toString(); //paymentRequestDTO.getOrderId();
        // 订单名称，必填
        String subject = "企业服务订单-" + out_trade_no;
        //System.out.println(subject);
        // 付款金额，必填
        String total_amount = paymentRequestDTO.getTransactionTotal();
        // 商品描述，可空
        String body = paymentRequestDTO.getOrderDescription();
        // 超时时间 可空
        String timeout_express="30m";
        // 销售产品码 必填
        String product_code = "QUICK_WAP_WAY";
        /**********************/
        // SDK 公共请求类，包含公共请求参数，以及封装了签名与验签，开发者无需关注签名与验签
        //调用RSA签名方式
        //AlipayClient client = new DefaultAlipayClient(AlipayConfig.URL, AlipayConfig.APPID, AlipayConfig.RSA_PRIVATE_KEY, AlipayConfig.FORMAT, AlipayConfig.CHARSET, AlipayConfig.ALIPAY_PUBLIC_KEY,AlipayConfig.SIGNTYPE);
        AlipayTradeWapPayRequest alipay_request = new AlipayTradeWapPayRequest();

        // 封装请求支付信息
        AlipayTradeWapPayModel model=new AlipayTradeWapPayModel();
        model.setOutTradeNo(out_trade_no);
        model.setSubject(subject);
        model.setTotalAmount(total_amount);
        model.setBody(body);
        model.setTimeoutExpress(timeout_express);
        model.setProductCode(product_code);
        alipay_request.setBizModel(model);

        // 设置异步通知地址
        alipay_request.setNotifyUrl(alipayConfiguration.getNotifyUrl());
        // 设置同步地址
        alipay_request.setReturnUrl(alipayConfiguration.getReturnUrl() + "/" + paymentRequestDTO.getOrderId());

        // form表单生产
        String form = "";
        try {
            // 调用SDK生成表单
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
            response.getWriter().write(form);//直接将完整的表单html输出到页面
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
