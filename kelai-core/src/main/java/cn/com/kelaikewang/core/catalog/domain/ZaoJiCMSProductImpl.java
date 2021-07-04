package cn.com.kelaikewang.core.catalog.domain;

import cn.com.kelaikewang.core.logistics.domain.ShippingTemplate;
import cn.com.kelaikewang.core.logistics.domain.ShippingTemplateImpl;
import cn.com.kelaikewang.core.article.domain.Article;
import cn.com.kelaikewang.core.article.domain.ArticleImpl;
import org.broadleafcommerce.common.presentation.*;
import org.broadleafcommerce.common.presentation.client.AddMethodType;
import org.broadleafcommerce.common.presentation.client.OperationType;
import org.broadleafcommerce.core.catalog.domain.ProductImpl;

import javax.persistence.*;
import java.util.List;

@Entity
@Table(name = "ZJCMS_PRODUCT")
@PrimaryKeyJoinColumn(name = "PRODUCT_ID")
@AdminPresentationClass(populateToOneFields = PopulateToOneFieldsEnum.TRUE, friendlyName = "ZaoJiCMSProductImpl_Logistics")
public class ZaoJiCMSProductImpl extends ProductImpl implements ZaoJiCMSProduct{

    @ManyToOne(targetEntity = ShippingTemplateImpl.class)
    @JoinColumn(name = "SHIPPING_TMPL_ID",nullable = false)
    @AdminPresentation(friendlyName = "ProductImpl_Product_Shipping_Template", order = Integer.MAX_VALUE,
            tab = TabName.Shipping,
            group = GroupName.ShippingDimensions,prominent = true)
    @AdminPresentationToOneLookup(lookupDisplayProperty = "name")
    protected ShippingTemplate shippingTemplate;

    @OneToMany(fetch = FetchType.LAZY,targetEntity = ProductColorOptionValueMediaImpl.class,mappedBy = "product")
    @AdminPresentationCollection(order = 1000,
            tab = TabName.ProductOptions,
            friendlyName = "ZaoJiCMSProductImpl_ColorOptionValueMedia",
            addType = AddMethodType.PERSIST,
            operationTypes = @AdminPresentationOperationTypes(removeType = OperationType.NONDESTRUCTIVEREMOVE)
    )
    protected List<ProductColorOptionValueMedia> colorOptionValueMedia;

    @ManyToMany(fetch = FetchType.LAZY,targetEntity = ArticleImpl.class)
    @JoinTable(name = "ZJCMS_ARTICLE_PRODUCT_MAPPING",
            joinColumns ={
                    @JoinColumn(name =  "PRODUCT_ID",referencedColumnName = "PRODUCT_ID")
            },
            inverseJoinColumns = {
                    @JoinColumn(name = "ARTICLE_ID",referencedColumnName = "ARTICLE_ID")
            })
    protected List<Article> articles;

    @Override
    public ShippingTemplate getShippingTemplate() {
        return shippingTemplate;
    }

    @Override
    public void setShippingTemplate(ShippingTemplate shippingTemplate) {
        this.shippingTemplate = shippingTemplate;
    }

    @Override
    public List<ProductColorOptionValueMedia> getProductColorOptionValueMedia() {
        return colorOptionValueMedia;
    }

    @Override
    public void setProductColorOptionValueMedia(List<ProductColorOptionValueMedia> productColorOptionValueMedia) {
        this.colorOptionValueMedia = productColorOptionValueMedia;
    }

    public List<Article> getArticles() {
        return articles;
    }

    public void setArticles(List<Article> articles) {
        this.articles = articles;
    }
}
