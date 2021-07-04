package cn.com.kelaikewang.core.logistics.dao;

import cn.com.kelaikewang.core.logistics.domain.ShippingPricingRule;
import cn.com.kelaikewang.infrastructure.dao.BaseDao;

public interface ShippingPricingRuleDao extends BaseDao<ShippingPricingRule> {
    ShippingPricingRule getOrderItemShippingPricingRule(long orderItemId);
}
