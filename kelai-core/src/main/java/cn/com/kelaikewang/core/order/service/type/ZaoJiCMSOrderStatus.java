package cn.com.kelaikewang.core.order.service.type;

import org.broadleafcommerce.core.order.service.type.OrderStatus;
import org.springframework.stereotype.Component;

@Component
public class ZaoJiCMSOrderStatus extends OrderStatus {
    /**
     * 已付款/待发货，原版本SUBMITTED为已经付款
     */
    public static final OrderStatus PAID = new OrderStatus("PAID", "待发货", false);
    /**
     * 卖家已发货
     */
    public static final OrderStatus SHIPPED = new OrderStatus("SHIPPED", "待收货", false);
    /**
     * 买家已收货
     */
    public static final OrderStatus RECEIVED = new OrderStatus("RECEIVED","已收货",false);
    /**
     * 退款申请处理中
     */
    public static final OrderStatus REFUND_PROCESSING = new OrderStatus("REFUND_PROCESSING","退款处理中",false);

    /**
     * 商家拒绝退款
     */
    public static final OrderStatus REFUND_REJECTED = new OrderStatus("REFUND_REJECTED","商家拒绝退款",false);
    /**
     * 商家已退款
     */
    public static final OrderStatus REFUNDED = new OrderStatus("REFUNDED","商家已退款",false);

}
