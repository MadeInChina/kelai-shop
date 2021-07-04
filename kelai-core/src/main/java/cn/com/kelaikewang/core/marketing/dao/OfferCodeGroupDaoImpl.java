package cn.com.kelaikewang.core.marketing.dao;

import cn.com.kelaikewang.core.marketing.domain.OfferCodeGroup;
import cn.com.kelaikewang.infrastructure.dao.BaseDaoImpl;
import org.springframework.stereotype.Repository;

@Repository("zjcmsOfferCodeGroupDao")
public class OfferCodeGroupDaoImpl extends BaseDaoImpl<OfferCodeGroup> implements OfferCodeGroupDao {

    @Override
    public Class<OfferCodeGroup> getModelClass() {
        return OfferCodeGroup.class;
    }
}
