package cn.com.kelaikewang.core.logistics.domain;

import cn.com.kelaikewang.core.order.domain.ZaoJiCMSOrderItem;
import cn.com.kelaikewang.core.order.domain.ZaoJiCMSOrderItemImpl;
import org.broadleafcommerce.common.presentation.*;
import org.broadleafcommerce.common.presentation.client.AddMethodType;
import org.broadleafcommerce.common.presentation.client.OperationType;
import org.broadleafcommerce.common.presentation.client.VisibilityEnum;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.math.BigDecimal;
import java.util.List;

@Entity
@Table(name = "ZJCMS_SHIPPING_PRICING_RULE")
public class ShippingPricingRuleImpl implements ShippingPricingRule,ShippingRuleAdminPresentation{
    @Id
    @Column(name = "ID")
    @GeneratedValue(generator = "ShippingTemplateShippingMethodId")
    @GenericGenerator(
            name="ShippingTemplateShippingMethodId",
            strategy="org.broadleafcommerce.common.persistence.IdOverrideTableGenerator",
            parameters = {
                    @org.hibernate.annotations.Parameter(name="segment_value", value="ShippingTemplateShippingMethodImpl"),
                    @org.hibernate.annotations.Parameter(name="entity_name", value="ShippingTemplateShippingMethodImpl")
            }
    )
    @AdminPresentation(visibility = VisibilityEnum.HIDDEN_ALL,gridOrder = 1)
    private Long id;
    @ManyToOne(fetch = FetchType.LAZY,targetEntity = ShippingMethodImpl.class)
    @JoinColumn(name = "SHIPPING_METHOD_ID",nullable = false)
    @AdminPresentation(friendlyName = "ShippingPricingRuleImpl_Name",order = 1,prominent = true,gridOrder = 2)
    @AdminPresentationToOneLookup(lookupDisplayProperty = "name")
    private ShippingMethod shippingMethod;

    @ManyToOne(fetch = FetchType.LAZY,targetEntity = ShippingTemplateImpl.class)
    @JoinColumn(name = "SHIPPING_TMPL_ID")
    private ShippingTemplate shippingTemplate;


    @Column(name = "DEFAULT_QUANTITY",nullable = false)
    @AdminPresentation(friendlyName = "ShippingPricingRuleImpl_DefaultQuantity",group = GroupName.DefaultShippingPrice,order = 10,prominent = true,gridOrder = 3)
    protected Double defaultQuantity;

    @Column(name = "DEFAULT_PRICE",nullable = false)
    @AdminPresentation(friendlyName = "ShippingPricingRuleImpl_DefaultPrice", order = 11,group = GroupName.DefaultShippingPrice,prominent = true,gridOrder = 4)
    protected BigDecimal defaultPrice;

    @Column(name = "INCREMENT",nullable = false)
    @AdminPresentation(friendlyName = "ShippingPricingRuleImpl_Increment", order = 12,group = GroupName.DefaultShippingPrice,prominent = true,gridOrder = 5)
    protected Double increment;

    @Column(name = "INCREMENT_PRICE",nullable = false)
    @AdminPresentation(friendlyName = "ShippingPricingRuleImpl_IncrementPrice", order = 13,group = GroupName.DefaultShippingPrice,prominent = true,gridOrder = 6)
    protected BigDecimal incrementPrice;

    @OneToMany(fetch = FetchType.LAZY,mappedBy = "shippingPricingRule",targetEntity = ShippingPricingRuleItemImpl.class)
    @AdminPresentationCollection(friendlyName = "ShippingPricingRuleImpl_ShippingPricingRuleItems",
            addType = AddMethodType.PERSIST, order = 6000,
            operationTypes = @AdminPresentationOperationTypes(removeType = OperationType.NONDESTRUCTIVEREMOVE)
    )
    protected List<ShippingPricingRuleItem> shippingPricingRuleItems;

    @OneToMany(fetch = FetchType.LAZY,mappedBy = "shippingPricingRule",targetEntity = ZaoJiCMSOrderItemImpl.class)
    protected List<ZaoJiCMSOrderItem> orderItems;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public ShippingMethod getShippingMethod() {
        return shippingMethod;
    }

    public void setShippingMethod(ShippingMethod shippingMethod) {
        this.shippingMethod = shippingMethod;
    }

    public ShippingTemplate getShippingTemplate() {
        return shippingTemplate;
    }

    public void setShippingTemplate(ShippingTemplate shippingTemplate) {
        this.shippingTemplate = shippingTemplate;
    }


    public Double getDefaultQuantity() {
        return defaultQuantity;
    }

    public void setDefaultQuantity(Double defaultQuantity) {
        this.defaultQuantity = defaultQuantity;
    }

    public BigDecimal getDefaultPrice() {
        return defaultPrice;
    }

    public void setDefaultPrice(BigDecimal defaultPrice) {
        this.defaultPrice = defaultPrice;
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

    public List<ShippingPricingRuleItem> getShippingPricingRuleItems() {
        return shippingPricingRuleItems;
    }

    public void setShippingPricingRuleItems(List<ShippingPricingRuleItem> shippingPricingRuleItems) {
        this.shippingPricingRuleItems = shippingPricingRuleItems;
    }

    @Override
    public List<ZaoJiCMSOrderItem> getOrderItems() {
        return orderItems;
    }

    @Override
    public void setOrderItems(List<ZaoJiCMSOrderItem> orderItems) {
        this.orderItems = orderItems;
    }

    @Override
    public String getMainEntityName() {
        ShippingMethod shippingMethod = getShippingMethod();
        if (shippingMethod == null){
            return "";
        }
        return shippingMethod.getName();
    }
}
