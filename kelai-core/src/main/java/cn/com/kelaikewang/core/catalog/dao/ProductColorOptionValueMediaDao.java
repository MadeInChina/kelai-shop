package cn.com.kelaikewang.core.catalog.dao;

import cn.com.kelaikewang.core.catalog.domain.ProductColorOptionValueMedia;
import cn.com.kelaikewang.infrastructure.dao.BaseDao;

public interface ProductColorOptionValueMediaDao extends BaseDao<ProductColorOptionValueMedia> {
    boolean isColorOptionValueMediaExists(long productId,long optionValueId);
}
