package cn.com.kelaikewang.core.administrativeRegion.service;

import cn.com.kelaikewang.core.administrativeRegion.dao.StreetDao;
import cn.com.kelaikewang.core.administrativeRegion.domain.Street;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

@Service("zjcmsStreetService")
public class StreetServiceImpl implements StreetService {

    @Resource(name = "zjcmsStreetDao")
    private StreetDao streetDao;

    @Override
    public List<Street> listAllStreet() {
        return streetDao.listAllStreet();
    }

    @Override
    public List<Street> listStreetByRegionCode(String regionCode) {
        return streetDao.listStreetByRegionCode(regionCode);
    }
}
