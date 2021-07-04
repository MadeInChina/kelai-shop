package cn.com.kelaikewang.core.payment.domain;

import org.broadleafcommerce.core.payment.domain.OrderPaymentImpl;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.PrimaryKeyJoinColumn;
import javax.persistence.Table;

@Entity
@Table(name = "ZJCMS_ORDER_PAYMENT")
@PrimaryKeyJoinColumn(name = "ORDER_PAYMENT_ID")
public class ZaoJiCMSOrderPaymentImpl extends OrderPaymentImpl implements ZaoJiCMSOrderPayment {

    @Column(name = "PAYMENT_GATEWAY_TRADE_NO")
    private String paymentGatewayTradeNO;

    @Column(name = "UNIFIED_ORDER_ID")
    private Long unifiedOrderId;
    @Override
    public String getPaymentGatewayTradeNO() {
        return paymentGatewayTradeNO;
    }

    @Override
    public void setPaymentGatewayTradeNO(String paymentGatewayTradeNO) {
        this.paymentGatewayTradeNO = paymentGatewayTradeNO;
    }

    @Override
    public Long getUnifiedOrderId() {
        return unifiedOrderId;
    }

    @Override
    public void setUnifiedOrderId(Long unifiedOrderId) {
        this.unifiedOrderId = unifiedOrderId;
    }
}
