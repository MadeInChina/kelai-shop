package cn.com.kelaikewang.core.marketing.dao;

import cn.com.kelaikewang.core.marketing.domain.RecommendedProduct;
import cn.com.kelaikewang.infrastructure.dao.BaseDaoImpl;
import org.broadleafcommerce.core.catalog.domain.Product;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository("zjcmsRecommendedProductDao")
public class RecommendedProductDaoImpl extends BaseDaoImpl<RecommendedProduct> implements RecommendedProductDao {
    @Override
    public List<Product> readAllRecommendedProducts() {
        return em.createNamedQuery("BC_READ_ALL_RECOMMENDED_PRODUCT",Product.class).getResultList();
    }

    @Override
    public Class<RecommendedProduct> getModelClass() {
        return RecommendedProduct.class;
    }
}
