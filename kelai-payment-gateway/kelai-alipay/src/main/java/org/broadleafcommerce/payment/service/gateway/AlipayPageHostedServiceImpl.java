package org.broadleafcommerce.payment.service.gateway;

import com.alibaba.fastjson.JSON;
import com.alipay.api.AlipayApiException;
import com.alipay.api.AlipayClient;
import com.alipay.api.DefaultAlipayClient;
import com.alipay.api.request.AlipayTradeCloseRequest;
import com.alipay.api.request.AlipayTradePagePayRequest;
import com.alipay.api.response.AlipayTradeCloseResponse;
import com.alipay.api.response.AlipayTradePagePayResponse;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
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
import org.broadleafcommerce.vendor.alipay.web.controller.BroadleafAlipayController;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

@Service("blAlipayPageHostedService")
public class AlipayPageHostedServiceImpl extends AbstractPaymentGatewayHostedService implements PaymentGatewayHostedService {

    @Resource(name = "blAlipayConfiguration")
    private AlipayConfiguration alipayConfiguration;

    @Resource(name = "blAlipayClient")
    private DefaultAlipayClient alipayClient;

    @Resource(name = "zjcmsPaymentUnifiedOrderService")
    private PaymentUnifiedOrderService paymentUnifiedOrderService;

    protected static final Log LOG = LogFactory.getLog(AlipayPageHostedServiceImpl.class);

    @Override
    public PaymentResponseDTO requestHostedEndpoint(PaymentRequestDTO paymentRequestDTO) throws PaymentException {
      /*  PaymentResponseDTO paymentResponseDTO = new PaymentResponseDTO(PaymentType.THIRD_PARTY_ACCOUNT, AlipayPaymentGatewayType.ALIPAY);
        Date now = new Date();
        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.DATE,3);

        PaymentUnifiedOrder paymentUnifiedOrder = paymentUnifiedOrderService.getByOrderIdAndPaymentType(Long.valueOf(paymentRequestDTO.getOrderId()),"Alipay");
        //如果订单额度不一样，先关闭原订单后再重新下单
        if (paymentUnifiedOrder != null){
            LOG.info("orderId=" + paymentRequestDTO.getOrderId() + "已向支付宝下过单");

            BigDecimal total =  new BigDecimal(paymentRequestDTO.getTransactionTotal());

            if (paymentUnifiedOrder.getAmount().compareTo(total)  != 0){
                LOG.info("orderId=" + paymentRequestDTO.getOrderId() + "金额有变动，需要关闭支付订单后再重新下单");
                AlipayTradeCloseRequest request = new AlipayTradeCloseRequest();
                request.setBizContent("{" +
                        "    \"out_trade_no\":\"" + paymentRequestDTO.getOrderId() + "\"" +
                        "  }");
                try {
                    AlipayTradeCloseResponse response = alipayClient.execute(request);
                    if (response.isSuccess()){
                        LOG.info("orderId=" + paymentRequestDTO.getOrderId() + "关闭订单请求成功");
                        LOG.info("关闭订单支付宝网关返回：" + JSON.toJSONString(response));

                        return pageExecute(paymentRequestDTO);
                    }else {
                        LOG.info("orderId=" + paymentRequestDTO.getOrderId() + "关闭订单请求失败");
                        LOG.info("关闭订单支付宝网关返回：" + JSON.toJSONString(response));
                        paymentResponseDTO.successful(false);
                        paymentResponseDTO.valid(false);
                        return paymentResponseDTO;
                    }
                } catch (AlipayApiException e) {
                    e.printStackTrace();
                    LOG.info("orderId=" + paymentRequestDTO.getOrderId() + "关闭订单请求失败");
                    LOG.error("关闭订单异常" ,e);
                    paymentResponseDTO.successful(false);
                    paymentResponseDTO.valid(false);
                    return paymentResponseDTO;
                }
            }else {
                LOG.info("orderId=" + paymentRequestDTO.getOrderId() + "已向支付宝下过单，但是订单金额不变，再次下单");
                return pageExecute(paymentRequestDTO);
            }
        }else {*/
        //LOG.info("orderId=" + paymentRequestDTO.getOrderId() + "没有向支付宝下过单，直接下单");
        PaymentUnifiedOrder unifiedOrder = new PaymentUnifiedOrderImpl();
        unifiedOrder.setAmount(new BigDecimal(Double.valueOf(paymentRequestDTO.getTransactionTotal())));
        unifiedOrder.setOrderId(Long.valueOf(paymentRequestDTO.getOrderId()));
        unifiedOrder.setPaymentType("Alipay");
        //unifiedOrder.setResponse();
        Date now = new Date();
        unifiedOrder.setSubmitedDate(now);
        //unifiedOrder.setTimeExpire(calendar.getTime());
        PaymentUnifiedOrder response = paymentUnifiedOrderService.create(unifiedOrder);

        return pageExecute(paymentRequestDTO,response.getId());
        //}


    }
    private PaymentResponseDTO pageExecute(PaymentRequestDTO paymentRequestDTO,Long unifiedOrderId){
        PaymentResponseDTO paymentResponseDTO = new PaymentResponseDTO(PaymentType.THIRD_PARTY_ACCOUNT, AlipayPaymentGatewayType.ALIPAY);
        LOG.info("开始向支付宝下单,orderId=" + paymentRequestDTO.getOrderId());
        //设置请求参数
        AlipayTradePagePayRequest alipayRequest = new AlipayTradePagePayRequest();
        alipayRequest.setReturnUrl(alipayConfiguration.getReturnUrl() + "/" + paymentRequestDTO.getOrderId());
        alipayRequest.setNotifyUrl(alipayConfiguration.getNotifyUrl());

        //商户订单号，商户网站订单系统中唯一订单号，必填
        String out_trade_no = unifiedOrderId.toString(); //paymentRequestDTO.getOrderId();
        //付款金额，必填
        String total_amount = paymentRequestDTO.getTransactionTotal();
        //订单名称，必填
        String subject = "企业服务订单-" + out_trade_no;
        //商品描述，可空
        String body = paymentRequestDTO.getOrderDescription();

        alipayRequest.setBizContent("{\"out_trade_no\":\""+ out_trade_no +"\","
                + "\"total_amount\":\""+ total_amount +"\","
                + "\"subject\":\""+ subject +"\","
                + "\"body\":\""+ body +"\","
                //+ "\"timeout_express\":\"3d\","
                + "\"product_code\":\"FAST_INSTANT_TRADE_PAY\"}");

        //若想给BizContent增加其他可选请求参数，以增加自定义超时时间参数timeout_express来举例说明
        //alipayRequest.setBizContent("{\"out_trade_no\":\""+ out_trade_no +"\","
        //		+ "\"total_amount\":\""+ total_amount +"\","
        //		+ "\"subject\":\""+ subject +"\","
        //		+ "\"body\":\""+ body +"\","
        //		+ "\"timeout_express\":\"10m\","
        //		+ "\"product_code\":\"FAST_INSTANT_TRADE_PAY\"}");
        //请求参数可查阅【电脑网站支付的API文档-alipay.trade.page.pay-请求参数】章节
        try {
            //请求
            AlipayTradePagePayResponse response = alipayClient.pageExecute(alipayRequest);
            if (response.isSuccess()){
                LOG.info("orderId=" + paymentRequestDTO.getOrderId() + "向支付宝下单成功");
                paymentResponseDTO.responseMap("form",response.getBody());
                paymentResponseDTO.successful(true);
                paymentResponseDTO.valid(true);
                return paymentResponseDTO;
            }else {
                LOG.info("orderId=" + paymentRequestDTO.getOrderId() + "向支付宝下单失败");
                paymentResponseDTO.successful(false);
                paymentResponseDTO.valid(false);
            }
        } catch (AlipayApiException e) {
            LOG.error("orderId=" + paymentRequestDTO.getOrderId() + "向支付宝下单失败",e);
            e.printStackTrace();
            paymentResponseDTO.successful(false);
            paymentResponseDTO.valid(false);
        }
        return paymentResponseDTO;
    }
}
