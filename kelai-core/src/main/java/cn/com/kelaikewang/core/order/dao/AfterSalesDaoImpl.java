package cn.com.kelaikewang.core.order.dao;

import cn.com.kelaikewang.core.order.domain.AfterSales;
import cn.com.kelaikewang.infrastructure.dao.BaseDaoImpl;
import org.springframework.stereotype.Repository;

@Repository("zjcmsAfterSalesDao")
public class AfterSalesDaoImpl extends BaseDaoImpl<AfterSales> implements AfterSalesDao {
    @Override
    public Class<AfterSales> getModelClass() {
        return AfterSales.class;
    }
}
