package cn.com.kelaikewang.core.administrativeRegion.dao;

import cn.com.kelaikewang.core.administrativeRegion.domain.City;
import cn.com.kelaikewang.infrastructure.dao.BaseDao;

import java.util.List;

public interface CityDao extends BaseDao<City> {
    List<City> listAllCity();
    List<City> listCityByProvinceCode(String provinceCode);
}
