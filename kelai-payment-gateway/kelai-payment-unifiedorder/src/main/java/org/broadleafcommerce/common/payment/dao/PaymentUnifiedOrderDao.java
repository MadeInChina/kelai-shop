package org.broadleafcommerce.common.payment.dao;

import org.broadleafcommerce.common.payment.domain.PaymentUnifiedOrder;

public interface PaymentUnifiedOrderDao {
    PaymentUnifiedOrder save(PaymentUnifiedOrder paymentUnifiedOrder);
    PaymentUnifiedOrder getById(Long id);
}
