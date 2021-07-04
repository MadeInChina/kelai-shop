package cn.com.kelaikewang.core.order.dao;

import cn.com.kelaikewang.core.order.domain.ZaoJiCMSOrderItem;
import org.broadleafcommerce.core.order.dao.OrderItemDao;

public interface ZaoJiCMSOrderItemDao extends OrderItemDao {
    void createOrderItemShippingPricingRule(long orderItemId,Long ruleId);
    void updateOrderItemShippingPricingRule(long orderItemId,Long ruleId);
    boolean isOrderItemShippingPricingRuleExists(long orderItemId);
    boolean isOrderItemShippingPricingRule(long orderItemId,Long ruleId);
    ZaoJiCMSOrderItem getOrderItem(long orderItemId);
    //OrderItem getByTypeAndId(String type,long id) throws ClassNotFoundException;
}
