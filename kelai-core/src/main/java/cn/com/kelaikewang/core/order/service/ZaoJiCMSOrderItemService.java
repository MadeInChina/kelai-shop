package cn.com.kelaikewang.core.order.service;

import cn.com.kelaikewang.core.logistics.domain.ShippingPricingRule;
import cn.com.kelaikewang.core.order.dto.OrderItemShippingPricingRuleDTO;
import org.broadleafcommerce.core.order.service.OrderItemService;

import java.util.List;

public interface ZaoJiCMSOrderItemService extends OrderItemService {
    void saveOrderItemsShippingPricingRule(List<OrderItemShippingPricingRuleDTO> orderItemShippingPricingRuleDTOS);
    void saveOrderItemShippingPricingRule(OrderItemShippingPricingRuleDTO dto);
    boolean isOrderItemShippingPricingRule(long orderItemId,long ruleId);
    ShippingPricingRule getOrderItemShippingPricingRule(long orderItemId);
}
