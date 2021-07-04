package cn.com.kelaikewang.core.store.service;

import cn.com.kelaikewang.core.store.dao.ZaoJiCMSStoreDao;
import cn.com.kelaikewang.core.store.domain.ZaoJiCMSStore;
import cn.com.kelaikewang.core.store.dto.CityStoresDTO;
import cn.com.kelaikewang.core.store.dto.ZaoJiCMSStoreDTO;
import cn.com.kelaikewang.commons.lang.StringUtils;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Service("zjcmsStoreService")
public class ZaoJiCMSStoreServiceImpl implements ZaoJiCMSStoreService {

    @Resource(name = "zjcmsStoreDao")
    ZaoJiCMSStoreDao nextShopStoreDao;

    @Override
    public List<ZaoJiCMSStore> readAllStore() {
        return nextShopStoreDao.readAllStore();
    }

    @Override
    public List<Map> readAllCity() {
        return nextShopStoreDao.readAllCity();
    }

    @Override
    public List<CityStoresDTO> readAllCityStores() {
        List<Map> cities = nextShopStoreDao.readAllCity();

        List<CityStoresDTO> cityStoresDTOS = new ArrayList<>();
        for (Map map : cities){
            CityStoresDTO cityStoresDTO = new CityStoresDTO();
            String city = map.get("city").toString();
            cityStoresDTO.setCity(city);
            cityStoresDTO.setJianpin(StringUtils.chinese2jianpin(city));
            cityStoresDTO.setQuanpin(StringUtils.chinese2quanpin(city));

            String cityCode = map.get("cityCode").toString();
            cityStoresDTO.setCityCode(cityCode);
            List<ZaoJiCMSStore> stores = nextShopStoreDao.readAllStoreByCityCode(cityCode);
            List<ZaoJiCMSStoreDTO> storeDTOS = new ArrayList<>();
            for (ZaoJiCMSStore store : stores){
                ZaoJiCMSStoreDTO storeDTO = new ZaoJiCMSStoreDTO();
                storeDTO.setCity(store.getCity());
                storeDTO.setCityCode(store.getCityCode());
                storeDTO.setDetailedAddress(store.getDetailedAddress());
                storeDTO.setId(store.getId());
                storeDTO.setLatitude(store.getLatitude());
                storeDTO.setLongitude(store.getLongitude());
                storeDTO.setName(store.getName());
                storeDTO.setOpen(store.getOpen());
                storeDTO.setProvince(store.getProvince());
                storeDTO.setProvinceCode(store.getProvinceCode());
                storeDTO.setRegion(store.getRegion());
                storeDTO.setRegionCode(store.getRegionCode());
                storeDTO.setStoreHours(store.getStoreHours());
                storeDTO.setTel(store.getTel());

                storeDTOS.add(storeDTO);
            }
            cityStoresDTO.setStores(storeDTOS);

            cityStoresDTOS.add(cityStoresDTO);
        }
        return cityStoresDTOS;
    }


}
