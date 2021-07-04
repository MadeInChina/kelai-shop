package cn.com.kelaikewang.core.logistics.domain;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

public interface ShippingTemplate  extends Serializable {
    Long getId();
    void setId(Long id);
    String getName();
    void setName(String name);
    String getDeliveryTime();
    void setDeliveryTime(String deliveryTime);
    Boolean getFreeShipping();
    void setFreeShipping(Boolean freeShipping) ;
    String getPricingType();
    void setPricingType(String pricingType);

    List<ShippingPricingRule> getShippingPricingRules();

    void setShippingPricingRules(List<ShippingPricingRule> shippingPricingRules);
    String getStatus();
    void setStatus(String status);
    Date getCreated();
    void setCreated(Date created);
}
