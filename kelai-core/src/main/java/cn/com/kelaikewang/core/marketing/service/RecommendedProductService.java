package cn.com.kelaikewang.core.marketing.service;

import org.broadleafcommerce.core.catalog.domain.Product;

import java.util.List;

public interface RecommendedProductService {
    List<Product> readAllRecommendedProducts();
}
