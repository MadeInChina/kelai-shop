package cn.com.kelaikewang.core.marketing.domain;

import org.broadleafcommerce.core.catalog.domain.Product;

import java.io.Serializable;

public interface RecommendedProduct extends Serializable {
    Long getId();
    void setId(Long id);
    Product getProduct();
    void setProduct(Product product);
    Integer getPosition();
    void setPosition(Integer position);
}
