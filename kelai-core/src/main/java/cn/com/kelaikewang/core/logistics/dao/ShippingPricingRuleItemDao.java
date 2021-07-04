package cn.com.kelaikewang.core.logistics.dao;

import cn.com.kelaikewang.core.logistics.domain.ShippingPricingRuleItem;
import cn.com.kelaikewang.infrastructure.dao.BaseDao;

import java.util.List;

public interface ShippingPricingRuleItemDao extends BaseDao<ShippingPricingRuleItem> {
    List<ShippingPricingRuleItem> listRuleItemsByRuleId(long ruleId);
    List<ShippingPricingRuleItem> listRuleItemsByRuleIdAndRuleItemIdNotEq(long ruleId,long ruleIemIdNotEq);
}
