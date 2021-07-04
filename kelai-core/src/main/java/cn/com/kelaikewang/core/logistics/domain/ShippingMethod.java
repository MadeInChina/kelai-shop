package cn.com.kelaikewang.core.logistics.domain;

import org.broadleafcommerce.common.admin.domain.AdminMainEntity;

import java.io.Serializable;
import java.util.List;

public interface ShippingMethod extends Serializable, AdminMainEntity {
    Long getId();
    void setId(Long id);
    String getName();
    void setName(String name);
    List<ShippingPricingRule> getShippingPricingRules();
    void setShippingPricingRules(List<ShippingPricingRule> shippingPricingRules);

}
