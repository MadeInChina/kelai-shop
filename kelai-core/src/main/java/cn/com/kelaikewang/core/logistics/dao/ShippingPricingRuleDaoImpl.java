package cn.com.kelaikewang.core.logistics.dao;

import cn.com.kelaikewang.core.logistics.domain.ShippingPricingRule;
import cn.com.kelaikewang.core.logistics.domain.ShippingPricingRuleImpl;
import cn.com.kelaikewang.infrastructure.dao.BaseDaoImpl;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository("zjcmsShippingPricingRuleDao")
public class ShippingPricingRuleDaoImpl extends BaseDaoImpl<ShippingPricingRule> implements ShippingPricingRuleDao{

    @Override
    public ShippingPricingRule getOrderItemShippingPricingRule(long orderItemId) {
        List<ShippingPricingRuleImpl> shippingPricingRules =
                em.createNativeQuery("SELECT * FROM ZJCMS_SHIPPING_PRICING_RULE r " +
                " JOIN ZJCMS_ORDER_ITEM o ON r.ID = o.ORDER_ITEM_ID " +
                " WHERE o.ORDER_ITEM_ID = :orderItemId", ShippingPricingRuleImpl.class)
                .setParameter("orderItemId",orderItemId)
                .getResultList();
        if (shippingPricingRules == null || shippingPricingRules.isEmpty()){
            return null;
        }
        return shippingPricingRules.get(0);
    }

    @Override
    public Class<ShippingPricingRule> getModelClass() {
        return ShippingPricingRule.class;
    }
}
