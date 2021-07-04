package cn.com.kelaikewang.core.marketing.dao;

import cn.com.kelaikewang.core.marketing.domain.RecommendedProduct;
import cn.com.kelaikewang.infrastructure.dao.BaseDao;
import org.broadleafcommerce.core.catalog.domain.Product;

import java.util.List;

public interface RecommendedProductDao extends BaseDao<RecommendedProduct> {
    List<Product> readAllRecommendedProducts();
}
