package cn.com.kelaikewang.core.administrativeRegion.dao;

import cn.com.kelaikewang.core.administrativeRegion.domain.Region;
import cn.com.kelaikewang.infrastructure.dao.BaseDao;

import java.util.List;

public interface RegionDao extends BaseDao<Region> {
    List<Region> listAllRegion();
    List<Region> listRegionByCityCode(String cityCode);
}
