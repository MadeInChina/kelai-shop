package org.broadleafcommerce.common.payment.domain;


import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.Type;

import javax.persistence.*;
import java.math.BigDecimal;
import java.util.Date;

@Entity
@Table(name = "ZJCMS_PAYMENT_UNIFIED_ORDER")
public class PaymentUnifiedOrderImpl implements PaymentUnifiedOrder {

    @Id
    @Column(name = "UNIFIED_ORDER_ID")
    private Long id;

    //@Id
    @Column(name = "ORDER_ID")
    private Long orderId;

    //@Id
    @Column(name = "PAYMENT_TYPE")
    private String paymentType;

    @Column(name = "AMOUNT")
    private BigDecimal amount;


    @Column(name = "SUBMITED_DATE")
    private Date submitedDate;

    @Override
    public Long getId() {
        return this.id;
    }

    @Override
    public void setId(Long id) {
        this.id = id;
    }

    @Override
    public Long getOrderId() {
        return this.orderId;
    }

    @Override
    public void setOrderId(Long orderId) {
        this.orderId = orderId;
    }

    @Override
    public BigDecimal getAmount() {
        return this.amount;
    }

    @Override
    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }

    @Override
    public Date getSubmitedDate() {
        return this.submitedDate;
    }

    @Override
    public void setSubmitedDate(Date submitedDate) {
        this.submitedDate = submitedDate;
    }

    @Override
    public String getPaymentType() {
        return this.paymentType;
    }

    @Override
    public void setPaymentType(String paymentType) {
        this.paymentType = paymentType;
    }

}
