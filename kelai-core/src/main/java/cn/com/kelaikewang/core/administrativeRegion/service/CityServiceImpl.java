package cn.com.kelaikewang.core.administrativeRegion.service;

import cn.com.kelaikewang.core.administrativeRegion.dao.CityDao;
import cn.com.kelaikewang.core.administrativeRegion.domain.City;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

@Service("zjcmsCityService")
public class CityServiceImpl implements CityService {
    @Resource(name = "zjcmsCityDao")
    private CityDao cityDao;

    @Override
    public List<City> listAllCity() {
        return cityDao.listAllCity();
    }

    @Override
    public List<City> listCityByProvinceCode(String provinceCode) {
        return cityDao.listCityByProvinceCode(provinceCode);
    }


}
