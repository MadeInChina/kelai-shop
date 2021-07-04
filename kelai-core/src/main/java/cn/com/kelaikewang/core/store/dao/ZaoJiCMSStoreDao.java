package cn.com.kelaikewang.core.store.dao;

import cn.com.kelaikewang.core.store.domain.ZaoJiCMSStore;
import cn.com.kelaikewang.infrastructure.dao.BaseDao;

import java.util.List;
import java.util.Map;

public interface ZaoJiCMSStoreDao extends BaseDao<ZaoJiCMSStore> {
    List<ZaoJiCMSStore> readAllStore();
    List<Map> readAllCity();
    List<ZaoJiCMSStore> readAllStoreByCityCode(String cityCode);
}
