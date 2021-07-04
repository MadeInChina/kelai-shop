package cn.com.kelaikewang.core.administrativeRegion.service;

import cn.com.kelaikewang.core.administrativeRegion.domain.Street;

import java.util.List;

public interface StreetService {
    List<Street> listAllStreet();
    List<Street> listStreetByRegionCode(String regionCode);
}
