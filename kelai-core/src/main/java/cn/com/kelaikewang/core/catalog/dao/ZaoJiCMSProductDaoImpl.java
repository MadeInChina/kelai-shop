package cn.com.kelaikewang.core.catalog.dao;

import org.broadleafcommerce.core.catalog.dao.ProductDaoImpl;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository("zjcmsProductDao")
public class ZaoJiCMSProductDaoImpl extends ProductDaoImpl implements ZaoJiCMSProductDao {

    @Override
    public List<Long> readAllProductBundleId() {
        return em.createNamedQuery("BC_READ_ALL_PRODUCT_BUNDLE_PRODUCT_ID", Long.class).getResultList();
    }

    @Override
    public List<Object[]> readTopSaleProduct(int top) {
        return em.createNamedQuery("BC_READ_TOP_SALE_PRODUCT").setMaxResults(top).getResultList();
    }
}
