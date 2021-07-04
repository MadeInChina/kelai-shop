package cn.com.kelaikewang.core.marketing.domain;

import org.broadleafcommerce.common.presentation.AdminPresentation;
import org.broadleafcommerce.common.presentation.AdminPresentationClass;
import org.broadleafcommerce.common.presentation.AdminPresentationToOneLookup;
import org.broadleafcommerce.core.catalog.domain.Product;
import org.broadleafcommerce.core.catalog.domain.ProductImpl;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;

/**
 * 首页推荐商品，作为营销模块的一个功能
 */
@Table(name = "ZJCMS_RECOMMENDED_PRODUCT")
@Entity
@AdminPresentationClass(friendlyName = "RecommendedProductImpl")
public class RecommendedProductImpl implements RecommendedProduct {
    @Id
    @Column(name = "RECOMMENDED_PRODUCT_ID")
    @GeneratedValue(generator = "RecommendedProductId")
    @GenericGenerator(
            name="RecommendedProductId",
            strategy="org.broadleafcommerce.common.persistence.IdOverrideTableGenerator",
            parameters = {
                    @org.hibernate.annotations.Parameter(name="segment_value", value="RecommendedProductImpl"),
                    @org.hibernate.annotations.Parameter(name="entity_name", value="RecommendedProductImpl")
            }
    )
    private Long id;
    @OneToOne(fetch = FetchType.LAZY,targetEntity = ProductImpl.class)
    @JoinColumn(name = "PRODUCT_ID",nullable = false)
    @AdminPresentation(prominent = true,order = 1,gridOrder = 1,friendlyName = "RecommendedProductImpl_Name")
    @AdminPresentationToOneLookup(lookupDisplayProperty = "defaultSku.name")
    private Product product;
    @Column(name = "POSITION",nullable = false)

    @AdminPresentation(prominent = true,order = 3,gridOrder = 3,friendlyName = "RecommendedProductImpl_Order")
    private Integer position;
    @Override
    public Long getId() {
        return id;
    }

    @Override
    public void setId(Long id) {
        this.id = id;
    }

    @Override
    public Product getProduct() {
        return product;
    }

    @Override
    public void setProduct(Product product) {
        this.product = product;
    }

    @Override
    public Integer getPosition() {
        return position;
    }

    @Override
    public void setPosition(Integer position) {
        this.position = position;
    }
}
