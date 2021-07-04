package cn.com.kelaikewang.core.store.service;

import cn.com.kelaikewang.core.store.domain.ZaoJiCMSStore;
import cn.com.kelaikewang.core.store.dto.CityStoresDTO;

import java.util.List;
import java.util.Map;

public interface ZaoJiCMSStoreService {
    List<ZaoJiCMSStore> readAllStore();
    List<Map> readAllCity();
    List<CityStoresDTO> readAllCityStores();
}
