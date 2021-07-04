package cn.com.kelaikewang.core.logistics.domain;

import cn.com.kelaikewang.core.order.domain.ZaoJiCMSOrderItem;
import org.broadleafcommerce.common.admin.domain.AdminMainEntity;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.List;

public interface ShippingPricingRule extends AdminMainEntity,Serializable {
    Long getId();

    void setId(Long id);

    ShippingMethod getShippingMethod();

    void setShippingMethod(ShippingMethod shippingMethod);

    ShippingTemplate getShippingTemplate();

    void setShippingTemplate(ShippingTemplate shippingTemplate);

    Double getDefaultQuantity();

    void setDefaultQuantity(Double defaultQuantity);

    BigDecimal getDefaultPrice();

    void setDefaultPrice(BigDecimal defaultPrice);
    Double getIncrement();

    void setIncrement(Double increment);

    BigDecimal getIncrementPrice();

    void setIncrementPrice(BigDecimal incrementPrice);

    List<ShippingPricingRuleItem> getShippingPricingRuleItems();

    void setShippingPricingRuleItems(List<ShippingPricingRuleItem> shippingPricingRuleItems);

    /*ShippingPricingRule getShippingPricingRule();

    void setShippingPricingRule(ShippingPricingRule shippingPricingRule);*/

    List<ZaoJiCMSOrderItem> getOrderItems();
    void setOrderItems(List<ZaoJiCMSOrderItem> orderItems);
}
