package cn.com.kelaikewang.core.logistics.domain;

import org.broadleafcommerce.common.presentation.*;
import org.broadleafcommerce.common.presentation.client.AddMethodType;
import org.broadleafcommerce.common.presentation.client.OperationType;
import org.broadleafcommerce.common.presentation.client.SupportedFieldType;
import org.broadleafcommerce.common.presentation.client.VisibilityEnum;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.util.Date;
import java.util.List;

@Table(name = "ZJCMS_SHIPPING_TEMPLATE")
@Entity
public class ShippingTemplateImpl implements ShippingTemplate,ShippingTemplateAdminPresentation{
    @Id
    @GeneratedValue(generator = "ShippingTemplateId")
    @GenericGenerator(
            name="ShippingTemplateId",
            strategy="org.broadleafcommerce.common.persistence.IdOverrideTableGenerator",
            parameters = {
                    @org.hibernate.annotations.Parameter(name="segment_value", value="ShippingTemplateImpl"),
                    @org.hibernate.annotations.Parameter(name="entity_name", value="ShippingTemplateImpl")
            }
    )
    @Column(name = "SHIPPING_TEMPLATE_ID")
    @AdminPresentation(friendlyName = "ShippingTemplateImpl_Id", visibility = VisibilityEnum.HIDDEN_ALL)
    protected Long id;

    @Column(name = "NAME",unique = true)
    @AdminPresentation(gridOrder = 1,friendlyName = "ShippingTemplateImpl_Name",prominent = true,order = 1)
    protected String name;

    @Column(name = "DELIVERY_TIME")
    @AdminPresentation(gridOrder = 2,order = 2,
            friendlyName = "ShippingTemplateImpl_DeliveryTime",
            prominent = true,
            broadleafEnumeration="cn.com.kelaikewang.core.logistics.service.type.DeliveryTimeType",
            fieldType = SupportedFieldType.BROADLEAF_ENUMERATION)
    protected String deliveryTime;

    @Column(name = "IS_FREE_SHIPPING")
    @AdminPresentation(gridOrder = 3,order = 3,
            friendlyName = "ShippingTemplateImpl_IsFreeShipping",
            prominent = true)
    protected Boolean isFreeShipping;

    @Column(name = "PRICING_TYPE",nullable = false)
    @AdminPresentation(gridOrder = 4,order = 4,
            friendlyName = "ShippingTemplateImpl_PricingType",
            broadleafEnumeration = "cn.com.kelaikewang.core.logistics.service.type.ShippingPricingType",
            prominent = true,
            fieldType = SupportedFieldType.BROADLEAF_ENUMERATION)
    protected String pricingType;

    @OneToMany(fetch = FetchType.LAZY,targetEntity = ShippingPricingRuleImpl.class,mappedBy = "shippingTemplate")
    @AdminPresentationCollection(friendlyName = "ShippingTemplateImpl_ShippingPricingRules",
            addType = AddMethodType.PERSIST, order = 6000,
            operationTypes = @AdminPresentationOperationTypes(removeType = OperationType.NONDESTRUCTIVEREMOVE)
    )
    protected List<ShippingPricingRule> shippingPricingRules;


    @Column(name = "STATUS",nullable = false)
    @AdminPresentation(prominent = true,
            gridOrder = 5,order = 5,
            friendlyName = "ShippingTemplateImpl_Status",
            broadleafEnumeration = "cn.com.kelaikewang.core.logistics.service.type.ShippingTemplateStatus",
            fieldType = SupportedFieldType.BROADLEAF_ENUMERATION)
    protected String status;

    @Column(name = "CREATED",nullable = false)
    @AdminPresentation(gridOrder = 6,order = 6,friendlyName = "ShippingTemplateImpl_Created",
            prominent = true,readOnly = true,defaultValue = "today")
    protected Date created = new Date();

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDeliveryTime() {
        return deliveryTime;
    }

    public void setDeliveryTime(String deliveryTime) {
        this.deliveryTime = deliveryTime;
    }

    @Override
    public Boolean getFreeShipping() {
        return isFreeShipping;
    }

    @Override
    public void setFreeShipping(Boolean freeShipping) {
        isFreeShipping = freeShipping;
    }

    @Override
    public String getPricingType() {
        return pricingType;
    }

    @Override
    public void setPricingType(String pricingType) {
        this.pricingType = pricingType;
    }


    public List<ShippingPricingRule> getShippingPricingRules() {
        return shippingPricingRules;
    }

    public void setShippingPricingRules(List<ShippingPricingRule> shippingPricingRules) {
        this.shippingPricingRules = shippingPricingRules;
    }

    @Override
    public String getStatus() {
        return status;
    }

    @Override
    public void setStatus(String status) {
        this.status = status;
    }

    @Override
    public Date getCreated() {
        if (created == null){
            return new Date();
        }
        return created;
    }

    @Override
    public void setCreated(Date created) {
        this.created = created;
    }
}
