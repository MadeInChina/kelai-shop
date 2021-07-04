package cn.com.kelaikewang.core.catalog.dao;

import cn.com.kelaikewang.core.catalog.domain.ProductColorOptionValueMedia;
import cn.com.kelaikewang.infrastructure.dao.BaseDaoImpl;
import org.broadleafcommerce.common.util.dao.TypedQueryBuilder;
import org.springframework.stereotype.Repository;

@Repository("zjcmsProductColorOptionValueMediaDao")
public class ProductColorOptionValueMediaDaoImpl extends BaseDaoImpl<ProductColorOptionValueMedia> implements ProductColorOptionValueMediaDao {
    @Override
    public boolean isColorOptionValueMediaExists(long productId, long optionValueId) {
        Long count = new TypedQueryBuilder<>(ProductColorOptionValueMedia.class, "opt")
                .addRestriction("opt.product.id", "=", productId)
                .addRestriction("opt.productOptionValue.id","=",optionValueId).toCountQuery(em).getSingleResult();
        return !count.equals(0L);
    }

    @Override
    public Class<ProductColorOptionValueMedia> getModelClass() {
        return ProductColorOptionValueMedia.class;
    }
}
