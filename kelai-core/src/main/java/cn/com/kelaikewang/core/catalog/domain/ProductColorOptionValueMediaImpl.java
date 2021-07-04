package cn.com.kelaikewang.core.catalog.domain;

import org.broadleafcommerce.common.presentation.AdminPresentation;
import org.broadleafcommerce.common.presentation.AdminPresentationClass;
import org.broadleafcommerce.common.presentation.AdminPresentationToOneLookup;
import org.broadleafcommerce.common.presentation.client.SupportedFieldType;
import org.broadleafcommerce.common.presentation.client.VisibilityEnum;
import org.broadleafcommerce.core.catalog.domain.ProductOptionValue;
import org.broadleafcommerce.core.catalog.domain.ProductOptionValueImpl;
import org.broadleafcommerce.menu.domain.MenuItemImpl;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;

@Entity
@Table(name = "ZJCMS_PRODUCT_COLOR_OPTION_VALUE_MEDIA")
@AdminPresentationClass(friendlyName = "ProductColorOptionValueMediaImpl")
public class ProductColorOptionValueMediaImpl implements ProductColorOptionValueMedia {

    @Id
    @GeneratedValue(generator = "ProductColorOptionValueMediaId")
    @GenericGenerator(
            name="ProductColorOptionValueMediaId",
            strategy="org.broadleafcommerce.common.persistence.IdOverrideTableGenerator",
            parameters = {
                    @org.hibernate.annotations.Parameter(name="segment_value", value="ProductColorOptionValueMediaImpl"),
                    @org.hibernate.annotations.Parameter(name="entity_name", value="ProductColorOptionValueMediaImpl")
            }
    )
    @Column(name = "ID")
    protected Long id;

    @ManyToOne(fetch = FetchType.LAZY,targetEntity = ZaoJiCMSProductImpl.class)
    @JoinColumn(name = "PRODUCT_ID")
    @AdminPresentation(excluded = true,visibility = VisibilityEnum.HIDDEN_ALL)
    protected ZaoJiCMSProduct product;

    @ManyToOne(fetch = FetchType.LAZY,targetEntity = ProductOptionValueImpl.class)
    @JoinColumn(name = "PRODUCT_OPTION_VALUE_ID")
    @AdminPresentationToOneLookup(lookupDisplayProperty = "attributeValue")
    @AdminPresentation(prominent = true,friendlyName = "ProductColorOptionValueMediaImpl_ProductColorOptionValue",order = 20)
    protected ProductOptionValue productOptionValue;

   /* @ManyToOne(fetch = FetchType.LAZY,targetEntity = MediaImpl.class)
    @JoinColumn(name = "MEDIA_ID")
    @AdminPresentationToOneLookup(lookupDisplayProperty = "title")
    @AdminPresentation(prominent = true,friendlyName = "ProductColorOptionValueMediaImpl_Media",order = 10)
    protected Media media;*/
   @Column(name = "IMAGE_URL")
   @AdminPresentation(friendlyName = "ProductColorOptionValueMediaImpl_ProductColorOptionValueImageUrl",
           order = MenuItemImpl.Presentation.FieldOrder.IMAGE_URL,
           fieldType = SupportedFieldType.ASSET_LOOKUP,prominent = true)
   protected String imageUrl;

    @Override
    public Long getId() {
        return id;
    }

    @Override
    public void setId(Long id) {
        this.id = id;
    }

    @Override
    public ZaoJiCMSProduct getProduct() {
        return product;
    }

    @Override
    public void setProduct(ZaoJiCMSProduct product) {
        this.product = product;
    }

    @Override
    public ProductOptionValue getProductOptionValue() {
        return productOptionValue;
    }

    @Override
    public void setProductOptionValue(ProductOptionValue productOptionValue) {
        this.productOptionValue = productOptionValue;
    }

    @Override
    public String getImageUrl() {
        return imageUrl;
    }

    @Override
    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }
}
