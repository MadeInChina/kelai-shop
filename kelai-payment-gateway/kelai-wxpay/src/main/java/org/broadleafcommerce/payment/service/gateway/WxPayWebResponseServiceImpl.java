package org.broadleafcommerce.payment.service.gateway;


import com.github.binarywang.wxpay.bean.notify.WxPayOrderNotifyResult;
import com.github.binarywang.wxpay.exception.WxPayException;
import com.github.binarywang.wxpay.service.WxPayService;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.broadleafcommerce.common.money.Money;
import org.broadleafcommerce.common.payment.PaymentTransactionType;
import org.broadleafcommerce.common.payment.PaymentType;
import org.broadleafcommerce.common.payment.domain.PaymentUnifiedOrder;
import org.broadleafcommerce.common.payment.dto.PaymentRequestDTO;
import org.broadleafcommerce.common.payment.dto.PaymentResponseDTO;
import org.broadleafcommerce.common.payment.service.AbstractPaymentGatewayWebResponseService;
import org.broadleafcommerce.common.payment.service.PaymentGatewayReportingService;
import org.broadleafcommerce.common.payment.service.PaymentGatewayWebResponseService;
import org.broadleafcommerce.common.payment.service.PaymentUnifiedOrderService;
import org.broadleafcommerce.common.vendor.service.exception.PaymentException;

import org.broadleafcommerce.vendor.wxpay.service.payment.WxPayPaymentGatewayType;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.math.BigDecimal;
import java.nio.charset.StandardCharsets;

@Service("blWxPayWebResponseService")
public class WxPayWebResponseServiceImpl extends AbstractPaymentGatewayWebResponseService implements PaymentGatewayWebResponseService {

    @Resource(name = "blWxPayConfiguration")
    private WxPayConfiguration alipayConfiguration;

    @Resource(name = "blWxPayReportingService")
    private PaymentGatewayReportingService paymentGatewayReportingService;

    @Resource(name = "wxPayService")
    private WxPayService wxPayService;

    @Resource(name = "zjcmsPaymentUnifiedOrderService")
    private PaymentUnifiedOrderService paymentUnifiedOrderService;

    protected static final Log LOG = LogFactory.getLog(WxPayWebResponseServiceImpl.class);

    @Override
    public PaymentResponseDTO translateWebResponse(HttpServletRequest request) throws PaymentException {

        PaymentResponseDTO paymentResponseDTO = new PaymentResponseDTO(PaymentType.THIRD_PARTY_ACCOUNT, WxPayPaymentGatewayType.WXPAY);
        paymentResponseDTO.paymentTransactionType(PaymentTransactionType.AUTHORIZE);
        try {

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
            WxPayOrderNotifyResult notifyResult = wxPayService.parseOrderNotifyResult(xmlData);
            PaymentUnifiedOrder paymentUnifiedOrder = paymentUnifiedOrderService.getById(Long.parseLong(notifyResult.getOutTradeNo()));
            paymentResponseDTO.orderId(paymentUnifiedOrder.getOrderId().toString());
            paymentResponseDTO.amount(new Money(BigDecimal.valueOf(notifyResult.getTotalFee()).divide(new BigDecimal(100)),"CNY"));
            paymentResponseDTO.responseMap("PAYMENT_GATEWAY_TRADE_NO",notifyResult.getTransactionId());
            paymentResponseDTO.responseMap("UNIFIED_ORDER_ID",notifyResult.getOutTradeNo());
            if ("SUCCESS".equals(notifyResult.getResultCode())) {
                paymentResponseDTO.successful(true);
                paymentResponseDTO.valid(true);
            }else {
                paymentResponseDTO.successful(false);
                paymentResponseDTO.valid(false);
            }
            paymentResponseDTO.responseMap("xmlData",xmlData);
            return paymentResponseDTO;


        } catch (WxPayException | IOException e) {
            e.printStackTrace();
            paymentResponseDTO.successful(false);
            paymentResponseDTO.valid(false);
            return paymentResponseDTO;
        }
    }
}
