package cn.com.kelaikewang.core.administrativeRegion.dao;

import cn.com.kelaikewang.core.administrativeRegion.domain.City;
import cn.com.kelaikewang.infrastructure.dao.BaseDaoImpl;
import org.apache.commons.lang3.StringUtils;
import org.broadleafcommerce.common.util.dao.TypedQueryBuilder;
import org.springframework.stereotype.Repository;

import javax.persistence.TypedQuery;
import java.util.List;

@Repository("zjcmsCityDao")
public class CityDaoImpl extends BaseDaoImpl<City> implements CityDao{
    @Override
    public List<City> listAllCity() {
        TypedQuery<City> query = new TypedQueryBuilder<>(City.class, "c")
                .toQuery(em);
        List<City> list = query.getResultList();
        return list;
    }

    @Override
    public List<City> listCityByProvinceCode(String provinceCode) {
        if (StringUtils.isEmpty(provinceCode)){
            return null;
        }
        TypedQuery<City> query = new TypedQueryBuilder<>(City.class, "c")
                .addRestriction("provinceCode","=",provinceCode)
                .toQuery(em);
        List<City> list = query.getResultList();
        return list;
    }

    @Override
    public Class<City> getModelClass() {
        return City.class;
    }
}
