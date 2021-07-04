package cn.com.kelaikewang.core.catalog.domain;

import org.broadleafcommerce.core.catalog.domain.ProductOptionValue;

import java.io.Serializable;

public interface ProductColorOptionValueMedia extends Serializable {
    Long getId();
    void setId(Long id);
    ZaoJiCMSProduct getProduct();
    void setProduct(ZaoJiCMSProduct product);
    ProductOptionValue getProductOptionValue();
    void setProductOptionValue(ProductOptionValue optionValue);
    String getImageUrl();
    void setImageUrl(String imageUrl);
}
