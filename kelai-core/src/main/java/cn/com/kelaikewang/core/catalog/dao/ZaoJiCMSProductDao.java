package cn.com.kelaikewang.core.catalog.dao;

import org.broadleafcommerce.core.catalog.dao.ProductDao;

import java.util.List;
import java.util.Map;

public interface ZaoJiCMSProductDao extends ProductDao{
    List<Long> readAllProductBundleId();
    List<Object[]> readTopSaleProduct(int top);
}
