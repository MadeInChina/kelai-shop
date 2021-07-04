package org.broadleafcommerce.common.payment.service;


import cn.com.kelaikewang.core.order.service.ZaoJiCMSOrderService;
import cn.com.kelaikewang.infrastructure.id.factory.IdFactory;
import org.broadleafcommerce.common.payment.dao.PaymentUnifiedOrderDao;
import org.broadleafcommerce.common.payment.domain.PaymentUnifiedOrder;
import org.broadleafcommerce.core.order.domain.Order;
import org.broadleafcommerce.core.order.service.type.OrderStatus;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;

@Service("zjcmsPaymentUnifiedOrderService")
public class PaymentUnifiedOrderServiceImpl implements PaymentUnifiedOrderService {

    @Resource(name = "zjcmsPaymentUnifiedOrderDao")
    protected PaymentUnifiedOrderDao paymentUnifiedOrderDao;
    @Resource(name = "blOrderService")
    protected ZaoJiCMSOrderService orderService;

    @Autowired
    private IdFactory idFactory;

    @Transactional("blTransactionManager")
    @Override
    public PaymentUnifiedOrder create(PaymentUnifiedOrder paymentUnifiedOrder) {
        Order order = orderService.findOrderById(paymentUnifiedOrder.getOrderId());
        if (order == null){
            throw new RuntimeException("订单不存在,id=" + paymentUnifiedOrder.getOrderId());
        }
        if (!OrderStatus.SUBMITTED.getType().equals(order.getStatus().getType())){
            throw new RuntimeException("订单状态为" + order.getStatus().getType() + "不允许支付,id=" + paymentUnifiedOrder.getOrderId());
        }
        paymentUnifiedOrder.setId(idFactory.getNextId());
        return paymentUnifiedOrderDao.save(paymentUnifiedOrder);
    }
    @Transactional("blTransactionManager")
    @Override
    public PaymentUnifiedOrder getById(long id) {
        return paymentUnifiedOrderDao.getById(id);
    }
}
