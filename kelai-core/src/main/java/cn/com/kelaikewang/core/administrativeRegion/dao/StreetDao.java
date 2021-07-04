package cn.com.kelaikewang.core.administrativeRegion.dao;

import cn.com.kelaikewang.core.administrativeRegion.domain.Street;
import cn.com.kelaikewang.infrastructure.dao.BaseDao;

import java.util.List;

public interface StreetDao extends BaseDao<Street> {
    List<Street> listAllStreet();
    List<Street> listStreetByRegionCode(String regionCode);
}
