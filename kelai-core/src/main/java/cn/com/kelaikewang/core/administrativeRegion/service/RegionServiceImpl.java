package cn.com.kelaikewang.core.administrativeRegion.service;

import cn.com.kelaikewang.core.administrativeRegion.dao.RegionDao;
import cn.com.kelaikewang.core.administrativeRegion.domain.Region;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

@Service("zjcmsRegionService")
public class RegionServiceImpl implements RegionService {

    @Resource(name = "zjcmsRegionDao")
    private RegionDao regionDao;
    @Override
    public List<Region> listAllRegion() {
        return regionDao.listAllRegion();
    }

    @Override
    public List<Region> listRegionByCityCode(String cityCode) {
        return regionDao.listRegionByCityCode(cityCode);
    }
}
