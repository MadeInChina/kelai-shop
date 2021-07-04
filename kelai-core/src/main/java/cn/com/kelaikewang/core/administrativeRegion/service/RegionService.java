package cn.com.kelaikewang.core.administrativeRegion.service;

import cn.com.kelaikewang.core.administrativeRegion.domain.Region;

import java.util.List;

public interface RegionService {
    List<Region> listAllRegion();
    List<Region> listRegionByCityCode(String cityCode);
}
