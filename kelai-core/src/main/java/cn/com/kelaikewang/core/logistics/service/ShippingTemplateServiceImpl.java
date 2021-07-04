package cn.com.kelaikewang.core.logistics.service;

import cn.com.kelaikewang.core.logistics.dao.ShippingPricingRuleItemDao;
import cn.com.kelaikewang.core.logistics.domain.ShippingPricingRuleItem;
import cn.com.kelaikewang.core.logistics.domain.ShippingTemplate;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

@Service("zjcmsShippingTemplateService")
public class ShippingTemplateServiceImpl implements ShippingTemplateService{

    @Resource(name = "zjcmsShippingPricingRuleItemDao")
    private ShippingPricingRuleItemDao shippingPricingRuleItemDao;

    @Override
    public List<ShippingPricingRuleItem> listRuleItemsByRuleId(long ruleId) {
        return shippingPricingRuleItemDao.listRuleItemsByRuleId(ruleId);
    }

    @Override
    public List<ShippingPricingRuleItem> listRuleItemsByRuleIdAndRuleItemIdNotEq(long ruleId, long ruleIemIdNotEq) {
        return shippingPricingRuleItemDao.listRuleItemsByRuleIdAndRuleItemIdNotEq(ruleId,ruleIemIdNotEq);
    }

    @Override
    public void createShippingTemplate(ShippingTemplate shippingTemplate) {

    }

    @Override
    public void updateShippingTemplate(ShippingTemplate shippingTemplate) {

    }

    @Override
    public void deleteShippingTemplate(long id) {

    }
}
