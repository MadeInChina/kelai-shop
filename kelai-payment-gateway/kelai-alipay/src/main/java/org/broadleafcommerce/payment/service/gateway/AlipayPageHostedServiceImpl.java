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
        //??????????????????????????????????????????????????????????????????
        if (paymentUnifiedOrder != null){
            LOG.info("orderId=" + paymentRequestDTO.getOrderId() + "????????????????????????");

            BigDecimal total =  new BigDecimal(paymentRequestDTO.getTransactionTotal());

            if (paymentUnifiedOrder.getAmount().compareTo(total)  != 0){
                LOG.info("orderId=" + paymentRequestDTO.getOrderId() + "????????????????????????????????????????????????????????????");
                AlipayTradeCloseRequest request = new AlipayTradeCloseRequest();
                request.setBizContent("{" +
                        "    \"out_trade_no\":\"" + paymentRequestDTO.getOrderId() + "\"" +
                        "  }");
                try {
                    AlipayTradeCloseResponse response = alipayClient.execute(request);
                    if (response.isSuccess()){
                        LOG.info("orderId=" + paymentRequestDTO.getOrderId() + "????????????????????????");
                        LOG.info("????????????????????????????????????" + JSON.toJSONString(response));

                        return pageExecute(paymentRequestDTO);
                    }else {
                        LOG.info("orderId=" + paymentRequestDTO.getOrderId() + "????????????????????????");
                        LOG.info("????????????????????????????????????" + JSON.toJSONString(response));
                        paymentResponseDTO.successful(false);
                        paymentResponseDTO.valid(false);
                        return paymentResponseDTO;
                    }
                } catch (AlipayApiException e) {
                    e.printStackTrace();
                    LOG.info("orderId=" + paymentRequestDTO.getOrderId() + "????????????????????????");
                    LOG.error("??????????????????" ,e);
                    paymentResponseDTO.successful(false);
                    paymentResponseDTO.valid(false);
                    return paymentResponseDTO;
                }
            }else {
                LOG.info("orderId=" + paymentRequestDTO.getOrderId() + "??????????????????????????????????????????????????????????????????");
                return pageExecute(paymentRequestDTO);
            }
        }else {*/
        //LOG.info("orderId=" + paymentRequestDTO.getOrderId() + "??????????????????????????????????????????");
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
        LOG.info("????????????????????????,orderId=" + paymentRequestDTO.getOrderId());
        //??????????????????
        AlipayTradePagePayRequest alipayRequest = new AlipayTradePagePayRequest();
        alipayRequest.setReturnUrl(alipayConfiguration.getReturnUrl() + "/" + paymentRequestDTO.getOrderId());
        alipayRequest.setNotifyUrl(alipayConfiguration.getNotifyUrl());

        //?????????????????????????????????????????????????????????????????????
        String out_trade_no = unifiedOrderId.toString(); //paymentRequestDTO.getOrderId();
        //?????????????????????
        String total_amount = paymentRequestDTO.getTransactionTotal();
        //?????????????????????
        String subject = "??????????????????-" + out_trade_no;
        //?????????????????????
        String body = paymentRequestDTO.getOrderDescription();

        alipayRequest.setBizContent("{\"out_trade_no\":\""+ out_trade_no +"\","
                + "\"total_amount\":\""+ total_amount +"\","
                + "\"subject\":\""+ subject +"\","
                + "\"body\":\""+ body +"\","
                //+ "\"timeout_express\":\"3d\","
                + "\"product_code\":\"FAST_INSTANT_TRADE_PAY\"}");

        //?????????BizContent?????????????????????????????????????????????????????????????????????timeout_express???????????????
        //alipayRequest.setBizContent("{\"out_trade_no\":\""+ out_trade_no +"\","
        //		+ "\"total_amount\":\""+ total_amount +"\","
        //		+ "\"subject\":\""+ subject +"\","
        //		+ "\"body\":\""+ body +"\","
        //		+ "\"timeout_express\":\"10m\","
        //		+ "\"product_code\":\"FAST_INSTANT_TRADE_PAY\"}");
        //?????????????????????????????????????????????API??????-alipay.trade.page.pay-?????????????????????
        try {
            //??????
            AlipayTradePagePayResponse response = alipayClient.pageExecute(alipayRequest);
            if (response.isSuccess()){
                LOG.info("orderId=" + paymentRequestDTO.getOrderId() + "????????????????????????");
                paymentResponseDTO.responseMap("form",response.getBody());
                paymentResponseDTO.successful(true);
                paymentResponseDTO.valid(true);
                return paymentResponseDTO;
            }else {
                LOG.info("orderId=" + paymentRequestDTO.getOrderId() + "????????????????????????");
                paymentResponseDTO.successful(false);
                paymentResponseDTO.valid(false);
            }
        } catch (AlipayApiException e) {
            LOG.error("orderId=" + paymentRequestDTO.getOrderId() + "????????????????????????",e);
            e.printStackTrace();
            paymentResponseDTO.successful(false);
            paymentResponseDTO.valid(false);
        }
        return paymentResponseDTO;
    }
}
