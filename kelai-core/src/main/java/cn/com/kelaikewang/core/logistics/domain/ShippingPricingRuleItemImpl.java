package cn.com.kelaikewang.core.logistics.domain;

import org.broadleafcommerce.common.presentation.AdminPresentation;
import org.broadleafcommerce.common.presentation.client.VisibilityEnum;
import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.Type;

import javax.persistence.*;
import java.math.BigDecimal;

@Entity
@Table(name = "ZJCMS_SHIPPING_PRICING_RULE_ITEM")
public class ShippingPricingRuleItemImpl implements ShippingPricingRuleItem{
    @Id
    @GeneratedValue(generator = "ShippingPricingRuleItemId")
    @GenericGenerator(
            name="ShippingPricingRuleItemId",
            strategy="org.broadleafcommerce.common.persistence.IdOverrideTableGenerator",
            parameters = {
                    @org.hibernate.annotations.Parameter(name="segment_value", value="ShippingPricingRuleItemImpl"),
                    @org.hibernate.annotations.Parameter(name="entity_name", value="ShippingPricingRuleItemImpl")
            }
    )
    @Column(name = "PRICING_RULE_ITEM_ID")
    @AdminPresentation(visibility = VisibilityEnum.HIDDEN_ALL,prominent = false)
    protected Long id;

    @AdminPresentation(friendlyName = "为指定地区城市设置运费",
            order = 1,
            gridOrder = 1,
            fieldComponentRendererTemplate="logistics-regions.html")
    @Column(name = "REGIONS",nullable = false,length = Integer.MAX_VALUE - 1)
    @Type(type = "text")
    protected String regions;

    @Column(name = "SELECTED_REGIONS",length = Integer.MAX_VALUE-1,nullable = false)
    @Type(type = "text")
    @AdminPresentation(friendlyName = "为指定地区城市设置运费",
            order = 1,
            prominent = true,
            gridOrder = 1,fieldComponentRendererTemplate="logistics-selected-regions.html")
    protected String selectedRegions;

    @Column(name = "SELECTED_REGION_IDS",length = Integer.MAX_VALUE-1,nullable = false)
    @AdminPresentation(friendlyName = "为指定地区城市设置运费",
            order = 1,
            gridOrder = 1,
            fieldComponentRendererTemplate="logistics-selected-region-ids.html")
    @Type(type = "text")
    protected String selectedRegionIds;

    @Column(name = "BOTTOM_PRICE",nullable = false)
    @AdminPresentation(friendlyName = "首费",order = 3,prominent = true,gridOrder = 2)
    protected BigDecimal bottomPrice;

    @Column(name = "BOTTOM_QUANTITY",nullable = false)
    @AdminPresentation(friendlyName = "首数",order = 2,prominent = true,gridOrder = 3)
    protected Double bottomQuantity;

    @Column(name = "INCREMENT",nullable = false)
    @AdminPresentation(friendlyName = "续件",order = 4,prominent = true,gridOrder = 4)
    protected Double increment;

    @Column(name = "INCREMENT_PRICE",nullable = false)
    @AdminPresentation(friendlyName = "续费",order = 5,prominent = true,gridOrder = 5)
    protected BigDecimal incrementPrice;

    @ManyToOne(fetch = FetchType.LAZY,targetEntity = ShippingPricingRuleImpl.class)
    @JoinColumn(name = "PRICING_RULE_ID")
    protected ShippingPricingRule shippingPricingRule;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getRegions() {
        return regions;
    }

    public void setRegions(String regions) {
        this.regions = regions;
    }

    public BigDecimal getBottomPrice() {
        return bottomPrice;
    }

    public void setBottomPrice(BigDecimal bottomPrice) {
        this.bottomPrice = bottomPrice;
    }

    public Double getBottomQuantity() {
        return bottomQuantity;
    }

    public void setBottomQuantity(Double bottomQuantity) {
        this.bottomQuantity = bottomQuantity;
    }

    public Double getIncrement() {
        return increment;
    }

    public void setIncrement(Double increment) {
        this.increment = increment;
    }

    public BigDecimal getIncrementPrice() {
        return incrementPrice;
    }

    public void setIncrementPrice(BigDecimal incrementPrice) {
        this.incrementPrice = incrementPrice;
    }

    public ShippingPricingRule getShippingPricingRule() {
        return shippingPricingRule;
    }

    public void setShippingPricingRule(ShippingPricingRule shippingPricingRule) {
        this.shippingPricingRule = shippingPricingRule;
    }

    public String getSelectedRegions() {
        return selectedRegions;
    }

    public void setSelectedRegions(String selectedRegions) {
        this.selectedRegions = selectedRegions;
    }

    public String getSelectedRegionIds() {
        return selectedRegionIds;
    }

    public void setSelectedRegionIds(String selectedRegionIds) {
        this.selectedRegionIds = selectedRegionIds;
    }
}
