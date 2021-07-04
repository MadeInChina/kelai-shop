package org.broadleafcommerce.common.payment.dao;

import org.broadleafcommerce.common.payment.domain.PaymentUnifiedOrder;
import org.broadleafcommerce.common.payment.domain.PaymentUnifiedOrderImpl;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

@Repository("zjcmsPaymentUnifiedOrderDao")
public class PaymentUnifiedOrderDaoImpl implements PaymentUnifiedOrderDao {

    @PersistenceContext(unitName = "blPU")
    protected EntityManager em;

    @Override
    public PaymentUnifiedOrder save(PaymentUnifiedOrder paymentUnifiedOrder) {
        PaymentUnifiedOrder response = em.merge(paymentUnifiedOrder);
        em.flush();
        return response;
    }

    @Override
    public PaymentUnifiedOrder getById(Long id) {
        return em.find(PaymentUnifiedOrderImpl.class,id);
    }

 /*   @Override
    public PaymentUnifiedOrder getByOrderIdAndPaymentType(Long orderId,String paymentType) {
        TypedQuery<PaymentUnifiedOrder> query = new TypedQueryBuilder<PaymentUnifiedOrder>(PaymentUnifiedOrder.class, "ord")
                .addRestriction("ord.orderId", "=", orderId)
                .addRestriction("ord.paymentType","=",paymentType)
                .toQuery(em);
        List<PaymentUnifiedOrder> list = query.getResultList();
        if (list == null || list.size() == 0){
            return null;
        }else {
            return list.get(0);
        }
    }*/
}
