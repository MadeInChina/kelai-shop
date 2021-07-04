package cn.com.kelaikewang.core.logistics.dao;

import cn.com.kelaikewang.core.logistics.domain.ExpressCompany;
import cn.com.kelaikewang.infrastructure.dao.BaseDaoImpl;
import org.springframework.stereotype.Repository;

@Repository("zjcmsExpressCompanyDao")
public class ExpressCompanyDaoImpl extends BaseDaoImpl<ExpressCompany> implements ExpressCompanyDao {
    @Override
    public Class<ExpressCompany> getModelClass() {
        return ExpressCompany.class;
    }
}
