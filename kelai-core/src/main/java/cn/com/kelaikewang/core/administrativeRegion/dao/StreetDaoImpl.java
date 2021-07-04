package cn.com.kelaikewang.core.administrativeRegion.dao;

import cn.com.kelaikewang.core.administrativeRegion.domain.Street;
import cn.com.kelaikewang.infrastructure.dao.BaseDaoImpl;
import org.apache.commons.lang3.StringUtils;
import org.broadleafcommerce.common.util.dao.TypedQueryBuilder;
import org.springframework.stereotype.Repository;

import javax.persistence.TypedQuery;
import java.util.List;

@Repository("zjcmsStreetDao")
public class StreetDaoImpl extends BaseDaoImpl<Street> implements StreetDao {

    @Override
    public List<Street> listAllStreet() {
        TypedQuery<Street> query = new TypedQueryBuilder<>(Street.class, "c")
                .toQuery(em);
        List<Street> list = query.getResultList();
        return list;
    }

    @Override
    public List<Street> listStreetByRegionCode(String regionCode) {
        if (StringUtils.isEmpty(regionCode)){
            return null;
        }
        TypedQuery<Street> query = new TypedQueryBuilder<>(Street.class, "c")
                .addRestriction("regionCode","=",regionCode)
                .toQuery(em);
        List<Street> list = query.getResultList();
        return list;
    }

    @Override
    public Class<Street> getModelClass() {
        return Street.class;
    }
}
