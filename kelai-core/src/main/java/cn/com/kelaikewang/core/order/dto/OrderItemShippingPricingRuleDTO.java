package cn.com.kelaikewang.core.order.dto;

import java.io.Serializable;

public class OrderItemShippingPricingRuleDTO implements Serializable {
    private Long orderItemId;
    private Long shippingPricingRuleId;

    public Long getOrderItemId() {
        return orderItemId;
    }

    public void setOrderItemId(Long orderItemId) {
        this.orderItemId = orderItemId;
    }

    public Long getShippingPricingRuleId() {
        return shippingPricingRuleId;
    }

    public void setShippingPricingRuleId(Long shippingPricingRuleId) {
        this.shippingPricingRuleId = shippingPricingRuleId;
    }
}
