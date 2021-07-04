package cn.com.kelaikewang.core.marketing.service;

import cn.com.kelaikewang.core.marketing.dao.RecommendedProductDao;
import org.broadleafcommerce.core.catalog.domain.Product;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

@Service("zjcmsRecommendedProductService")
public class RecommendedProductServiceImpl implements RecommendedProductService {
    @Resource(name = "zjcmsRecommendedProductDao")
    protected RecommendedProductDao recommendedProductDao;
    @Override
    public List<Product> readAllRecommendedProducts() {
        return recommendedProductDao.readAllRecommendedProducts();
    }
}
