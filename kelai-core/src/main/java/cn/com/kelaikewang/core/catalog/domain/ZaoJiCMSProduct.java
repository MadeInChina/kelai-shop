package cn.com.kelaikewang.core.catalog.domain;

import cn.com.kelaikewang.core.logistics.domain.ShippingTemplate;
import cn.com.kelaikewang.core.article.domain.Article;
import org.broadleafcommerce.core.catalog.domain.Product;

import java.util.List;

public interface ZaoJiCMSProduct extends Product {
    ShippingTemplate getShippingTemplate();
    void setShippingTemplate(ShippingTemplate shippingTemplate);
    List<ProductColorOptionValueMedia> getProductColorOptionValueMedia();
    void setProductColorOptionValueMedia(List<ProductColorOptionValueMedia> colorOptionValueMedia);
    List<Article> getArticles();

    void setArticles(List<Article> articles);
}
