package org.broadleafcommerce.common.payment.domain;

import sun.rmi.runtime.Log;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

public interface PaymentUnifiedOrder extends Serializable {
    Long getId();
    void setId(Long id);
    Long getOrderId();
    void setOrderId(Long orderId);
    BigDecimal getAmount();
    void setAmount(BigDecimal amount);
    Date getSubmitedDate();
    void setSubmitedDate(Date submitedDate);
    String getPaymentType();
    void setPaymentType(String paymentType);
}
