package org.broadleafcommerce.payment.service.gateway;

import com.alipay.api.AlipayApiException;
import com.alipay.api.AlipayClient;
import com.alipay.api.request.AlipayTradeQueryRequest;
import com.alipay.api.response.AlipayTradeQueryResponse;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
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
import org.broadleafcommerce.vendor.alipay.service.payment.AlipayPaymentGatewayType;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

@Service("blAlipayReportingService")
public class AlipayReportingServiceImpl extends AbstractPaymentGatewayReportingService implements PaymentGatewayReportingService {
    @Resource(name = "blAlipayClient")
    private AlipayClient alipayClient;
    @Resource(name = "blAlipayConfiguration")
    private AlipayConfiguration alipayConfiguration;

    @Resource(name = "zjcmsPaymentUnifiedOrderService")
    private PaymentUnifiedOrderService paymentUnifiedOrderService;

    protected static final Log LOG = LogFactory.getLog(AlipayReportingServiceImpl.class);

    @Override
    public PaymentResponseDTO findDetailsByTransaction(PaymentRequestDTO paymentRequestDTO) throws PaymentException {
        PaymentResponseDTO paymentResponseDTO = new PaymentResponseDTO(PaymentType.THIRD_PARTY_ACCOUNT, AlipayPaymentGatewayType.ALIPAY);
        paymentResponseDTO.paymentTransactionType(PaymentTransactionType.AUTHORIZE);
        try {
            Object unifiedOrderId = paymentRequestDTO.getAdditionalFields().get("unifiedOrderId");
            if (unifiedOrderId == null){
                LOG.error("unifiedOrderId = null");
                paymentResponseDTO.successful(false);
                paymentResponseDTO.valid(false);
                return paymentResponseDTO;
            }
            AlipayTradeQueryRequest request = new AlipayTradeQueryRequest();
            request.setBizContent("{\"out_trade_no\":\"" + unifiedOrderId + "\"}");
            AlipayTradeQueryResponse response = alipayClient.execute(request);
            if (response.isSuccess()) {
                //System.out.println("调用成功");
                //交易状态：
                // WAIT_BUYER_PAY（交易创建，等待买家付款）、
                // TRADE_CLOSED（未付款交易超时关闭，或支付完成后全额退款）、
                // TRADE_SUCCESS（交易支付成功）、
                // TRADE_FINISHED（交易结束，不可退款）
                String status = response.getTradeStatus();
                if ("TRADE_SUCCESS".equals(status) || "TRADE_FINISHED".equals(status)){
                    paymentResponseDTO.successful(true);
                    paymentResponseDTO.valid(true);
                    PaymentUnifiedOrder paymentUnifiedOrder = paymentUnifiedOrderService.getById(Long.valueOf(unifiedOrderId.toString()));
                    paymentResponseDTO.orderId(paymentUnifiedOrder.getOrderId().toString());
                    paymentResponseDTO.responseMap("PAYMENT_GATEWAY_TRADE_NO",response.getTradeNo());
                    paymentResponseDTO.responseMap("UNIFIED_ORDER_ID",unifiedOrderId.toString());
                    paymentResponseDTO.amount(new Money(response.getTotalAmount(),"CNY"));
                    LOG.info("交易成功");
                }else {
                    paymentResponseDTO.successful(false);
                    paymentResponseDTO.valid(false);
                    LOG.info("交易失败或者正在处理中");
                }
            } else {
                //System.out.println("调用失败");
                LOG.error("支付宝查询接口调用失败");
            }

        } catch (AlipayApiException e) {
            e.printStackTrace();
            paymentResponseDTO.successful(false);
            paymentResponseDTO.valid(false);
            LOG.error("支付宝查询接口调用异常：" + e.getMessage());
        }
        return paymentResponseDTO;
    }
}
