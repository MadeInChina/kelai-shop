package cn.com.kelaikewang.core.checkout.service;

import cn.com.kelaikewang.core.order.service.type.ZaoJiCMSOrderStatus;
import org.broadleafcommerce.core.checkout.service.CheckoutServiceImpl;
import org.broadleafcommerce.core.order.domain.Order;
import org.broadleafcommerce.core.order.service.type.OrderStatus;

public class ZaoJiCMSCheckoutServiceImpl extends CheckoutServiceImpl {
    @Override
    protected boolean hasOrderBeenCompleted(Order order) {
        //return super.hasOrderBeenCompleted(order);
        return ZaoJiCMSOrderStatus.PAID.equals(order.getStatus()) || OrderStatus.CANCELLED.equals(order.getStatus());
    }
}
