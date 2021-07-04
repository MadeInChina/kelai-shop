package org.broadleafcommerce.common.payment.service;

import org.broadleafcommerce.common.payment.domain.PaymentUnifiedOrder;

public interface PaymentUnifiedOrderService {
    PaymentUnifiedOrder create(PaymentUnifiedOrder paymentUnifiedOrder);
    PaymentUnifiedOrder getById(long id);
}
