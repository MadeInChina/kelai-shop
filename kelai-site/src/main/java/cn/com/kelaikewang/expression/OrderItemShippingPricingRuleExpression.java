package cn.com.kelaikewang.expression;

import cn.com.kelaikewang.core.order.service.ZaoJiCMSOrderItemService;
import org.broadleafcommerce.common.web.expression.BroadleafVariableExpression;
import org.broadleafcommerce.presentation.condition.ConditionalOnTemplating;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;

@Component("zjcmsOrderItemShippingPricingRuleExpression")
@ConditionalOnTemplating
public class OrderItemShippingPricingRuleExpression implements BroadleafVariableExpression {
    @Resource(name = "blOrderItemService")
    private ZaoJiCMSOrderItemService nextShopOrderItemService;
    @Override
    public String getName() {
        return "zjcmsOrderItemShippingPricingRuleExpression";
    }
    public boolean isOrderItemShippingPricingRule(long orderItemId,long ruleId){
        return nextShopOrderItemService.isOrderItemShippingPricingRule(orderItemId,ruleId);
    }
}
