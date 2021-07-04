package cn.com.kelaikewang.core.administrativeRegion.service;

import cn.com.kelaikewang.core.administrativeRegion.domain.City;

import java.util.List;

public interface CityService {
    List<City> listAllCity();
    List<City> listCityByProvinceCode(String provinceCode);
}
