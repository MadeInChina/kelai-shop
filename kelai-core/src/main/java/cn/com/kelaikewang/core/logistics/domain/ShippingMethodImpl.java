package cn.com.kelaikewang.core.logistics.domain;

import org.broadleafcommerce.common.presentation.AdminPresentation;
import org.broadleafcommerce.common.presentation.client.VisibilityEnum;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.util.List;

@Table(name = "ZJCMS_SHIPPING_METHOD")
@Entity
public class ShippingMethodImpl implements ShippingMethod{
    @Id
    @GeneratedValue(generator = "ShippingMethodId")
    @GenericGenerator(
            name="ShippingMethodId",
            strategy="org.broadleafcommerce.common.persistence.IdOverrideTableGenerator",
            parameters = {
                    @org.hibernate.annotations.Parameter(name="segment_value", value="ShippingMethodImpl"),
                    @org.hibernate.annotations.Parameter(name="entity_name", value="ShippingMethodImpl")
            }
    )
    @Column(name = "SHIPPING_METHOD_ID")
    @AdminPresentation(friendlyName = "ShippingMethodImpl_Id", visibility = VisibilityEnum.HIDDEN_ALL,gridOrder = 1)
    protected Long id;
    @Column(name = "NAME",unique = true,nullable = false)
    @AdminPresentation(prominent = true,friendlyName = "ShippingMethodImpl_Name",gridOrder = 2)
    protected String name;

    @OneToMany(fetch = FetchType.LAZY,mappedBy = "shippingMethod",targetEntity = ShippingPricingRuleImpl.class)
    protected List<ShippingPricingRule> shippingPricingRules;


    @Override
    public Long getId() {
        return id;
    }

    @Override
    public void setId(Long id) {
        this.id = id;
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public void setName(String name) {
        this.name = name;
    }


    public List<ShippingPricingRule> getShippingPricingRules() {
        return shippingPricingRules;
    }

    public void setShippingPricingRules(List<ShippingPricingRule> shippingPricingRules) {
        this.shippingPricingRules = shippingPricingRules;
    }

    @Override
    public String getMainEntityName() {
        return name;
    }
}
