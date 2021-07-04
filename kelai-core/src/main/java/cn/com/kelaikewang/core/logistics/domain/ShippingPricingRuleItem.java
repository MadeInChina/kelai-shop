package cn.com.kelaikewang.core.logistics.domain;

import java.io.Serializable;
import java.math.BigDecimal;

public interface ShippingPricingRuleItem  extends Serializable {
    Long getId() ;

    void setId(Long id);

    String getRegions();

    void setRegions(String regions);

    BigDecimal getBottomPrice();

    void setBottomPrice(BigDecimal bottomPrice);

    Double getBottomQuantity();

    void setBottomQuantity(Double bottomQuantity);

    Double getIncrement();

    void setIncrement(Double increment);

    BigDecimal getIncrementPrice();

    void setIncrementPrice(BigDecimal incrementPrice);

    ShippingPricingRule getShippingPricingRule();

    void setShippingPricingRule(ShippingPricingRule shippingPricingRule);

    String getSelectedRegions();

    void setSelectedRegions(String selectedRegions);
    String getSelectedRegionIds();

    void setSelectedRegionIds(String selectedRegionIds);
}
