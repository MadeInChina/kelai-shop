package cn.com.kelaikewang.core.administrativeRegion.dao;

import cn.com.kelaikewang.core.administrativeRegion.domain.Region;
import cn.com.kelaikewang.infrastructure.dao.BaseDaoImpl;
import org.apache.commons.lang3.StringUtils;
import org.broadleafcommerce.common.util.dao.TypedQueryBuilder;
import org.springframework.stereotype.Repository;

import javax.persistence.TypedQuery;
import java.util.List;

@Repository("zjcmsRegionDao")
public class RegionDaoImpl extends BaseDaoImpl<Region> implements RegionDao {

    @Override
    public List<Region> listAllRegion() {
        TypedQuery<Region> query = new TypedQueryBuilder<>(Region.class, "c")
                .toQuery(em);
        List<Region> list = query.getResultList();
        return list;
    }

    @Override
    public List<Region> listRegionByCityCode(String cityCode) {
        if (StringUtils.isEmpty(cityCode)){
            return null;
        }
        TypedQuery<Region> query = new TypedQueryBuilder<>(Region.class, "c")
                .addRestriction("cityCode","=",cityCode)
                .toQuery(em);
        List<Region> list = query.getResultList();
        return list;
    }

    @Override
    public Class<Region> getModelClass() {
        return Region.class;
    }
}
