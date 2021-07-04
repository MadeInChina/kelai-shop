package cn.com.kelaikewang.core.logistics.dao;

import cn.com.kelaikewang.core.logistics.domain.ShippingPricingRuleItem;
import cn.com.kelaikewang.infrastructure.dao.BaseDaoImpl;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository("zjcmsShippingPricingRuleItemDao")
public class ShippingPricingRuleItemDaoImpl extends BaseDaoImpl<ShippingPricingRuleItem> implements ShippingPricingRuleItemDao {

    @Override
    public List<ShippingPricingRuleItem> listRuleItemsByRuleId(long ruleId) {
        List<ShippingPricingRuleItem> shippingPricingRuleItems = em.createNamedQuery("BC_READ_RULE_ITEMS_BY_RULE_ID",ShippingPricingRuleItem.class)
                .setParameter("ruleId",ruleId)
                .getResultList();
        return shippingPricingRuleItems;
    }

    @Override
    public List<ShippingPricingRuleItem> listRuleItemsByRuleIdAndRuleItemIdNotEq(long ruleId, long ruleIemIdNotEq) {
        List<ShippingPricingRuleItem> shippingPricingRuleItems = em.createNamedQuery("BC_READ_RULE_ITEMS_BY_RULE_ID_AND_RULE_ITEM_ID_NOT_EQ",ShippingPricingRuleItem.class)
                .setParameter("ruleId",ruleId)
                .setParameter("ruleItemId",ruleIemIdNotEq)
                .getResultList();
        return shippingPricingRuleItems;
    }

    @Override
    public Class<ShippingPricingRuleItem> getModelClass() {
        return ShippingPricingRuleItem.class;
    }
}
