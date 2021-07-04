package cn.com.kelaikewang.core.logistics.service;

import cn.com.kelaikewang.core.logistics.domain.ShippingPricingRuleItem;
import cn.com.kelaikewang.core.logistics.domain.ShippingTemplate;

import java.util.List;

public interface ShippingTemplateService {
    List<ShippingPricingRuleItem> listRuleItemsByRuleId(long ruleId);
    List<ShippingPricingRuleItem> listRuleItemsByRuleIdAndRuleItemIdNotEq(long ruleId,long ruleIemIdNotEq);
    void createShippingTemplate(ShippingTemplate shippingTemplate);
    void updateShippingTemplate(ShippingTemplate shippingTemplate);
    void deleteShippingTemplate(long id);
}
