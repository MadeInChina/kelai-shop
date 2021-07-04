package org.broadleafcommerce.payment.service.gateway;


import com.github.binarywang.wxpay.bean.result.WxPayOrderQueryResult;
import com.github.binarywang.wxpay.exception.WxPayException;
import com.github.binarywang.wxpay.service.WxPayService;
import org.broadleafcommerce.common.money.Money;
import org.broadleafcommerce.common.payment.PaymentTransactionType;
import org.broadleafcommerce.common.payment.PaymentType;
import org.broadleafcommerce.common.payment.domain.PaymentUnifiedOrder;
import org.broadleafcommerce.common.payment.dto.PaymentRequestDTO;
import org.broadleafcommerce.common.payment.dto.PaymentResponseDTO;
import org.broadleafcommerce.common.payment.service.AbstractPaymentGatewayReportingService;
import org.broadleafcommerce.common.payment.service.PaymentGatewayReportingService;
import org.broadleafcommerce.common.payment.service.PaymentUnifiedOrderService;
import org.broadleafcommerce.common.vendor.service.exception.PaymentException;
import org.broadleafcommerce.vendor.wxpay.service.payment.WxPayPaymentGatewayType;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.math.BigDecimal;

@Service("blWxPayReportingService")
public class WxPayReportingServiceImpl extends AbstractPaymentGatewayReportingService implements PaymentGatewayReportingService {

    @Resource(name = "blWxPayConfiguration")
    private WxPayConfiguration alipayConfiguration;

    @Resource(name = "wxPayService")
    private WxPayService wxService;

    @Resource(name = "zjcmsPaymentUnifiedOrderService")
    private PaymentUnifiedOrderService paymentUnifiedOrderService;

    @Override
    public PaymentResponseDTO findDetailsByTransaction(PaymentRequestDTO paymentRequestDTO) throws PaymentException {
        PaymentResponseDTO paymentResponseDTO = new PaymentResponseDTO(PaymentType.THIRD_PARTY_ACCOUNT, WxPayPaymentGatewayType.WXPAY);
        paymentResponseDTO.paymentTransactionType(PaymentTransactionType.AUTHORIZE);

        try {
            Object unifiedOrderId = paymentRequestDTO.getAdditionalFields().get("unifiedOrderId");
            if (unifiedOrderId == null){
                paymentResponseDTO.successful(false);
                paymentResponseDTO.valid(false);
                return paymentResponseDTO;
            }
            WxPayOrderQueryResult wxPayOrderQueryResult = this.wxService.queryOrder(null, unifiedOrderId.toString());
            if ("SUCCESS".equals(wxPayOrderQueryResult.getTradeState())) {
                paymentResponseDTO.valid(true);
                paymentResponseDTO.successful(true);
                paymentResponseDTO.responseMap("PAYMENT_GATEWAY_TRADE_NO",wxPayOrderQueryResult.getTransactionId());
                paymentResponseDTO.responseMap("UNIFIED_ORDER_ID",unifiedOrderId.toString());
            }else {
                paymentResponseDTO.valid(false);
                paymentResponseDTO.successful(false);
            }
            PaymentUnifiedOrder paymentUnifiedOrder = paymentUnifiedOrderService.getById(Long.parseLong(unifiedOrderId.toString()));
            paymentResponseDTO.orderId(paymentUnifiedOrder.getOrderId().toString());
            Integer fen = wxPayOrderQueryResult.getTotalFee();
            paymentResponseDTO.amount(new Money(BigDecimal.valueOf(fen).divide(new BigDecimal(100)),"CNY"));

        } catch (WxPayException e) {
            e.printStackTrace();
            paymentResponseDTO.successful(false);
            paymentResponseDTO.valid(false);
        }

        return paymentResponseDTO;
    }
}
