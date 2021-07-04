package cn.com.kelaikewang.core.payment.domain;

import org.broadleafcommerce.core.payment.domain.OrderPayment;

public interface ZaoJiCMSOrderPayment extends OrderPayment {
    String getPaymentGatewayTradeNO();
    void setPaymentGatewayTradeNO(String paymentGatewayTradeNO);
    Long getUnifiedOrderId();
    void setUnifiedOrderId(Long unifiedOrderId);
}
