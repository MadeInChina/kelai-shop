package cn.com.kelaikewang.core.store.dao;

import cn.com.kelaikewang.core.store.domain.ZaoJiCMSStore;
import cn.com.kelaikewang.infrastructure.dao.BaseDaoImpl;
import org.broadleafcommerce.common.util.dao.TypedQueryBuilder;
import org.springframework.stereotype.Repository;

import javax.persistence.TypedQuery;
import java.util.List;
import java.util.Map;

@Repository("zjcmsStoreDao")
public class ZaoJiCMSStoreDaoImpl extends BaseDaoImpl<ZaoJiCMSStore> implements ZaoJiCMSStoreDao {

    @Override
    public List<ZaoJiCMSStore> readAllStore() {
        TypedQuery<ZaoJiCMSStore> query = new TypedQueryBuilder<ZaoJiCMSStore>(ZaoJiCMSStore.class, "store")
                .toQuery(em);

        List<ZaoJiCMSStore> list = query.getResultList();
        return list;
    }

    @Override
    public List<Map> readAllCity() {
        return em.createNamedQuery("BC_READ_ALL_STORE_CITY",Map.class).getResultList();
    }

    @Override
    public List<ZaoJiCMSStore> readAllStoreByCityCode(String cityCode) {
        return em.createNamedQuery("BC_READ_ALL_STORE_BY_CITY_CODE",ZaoJiCMSStore.class)
                .setParameter("cityCode",cityCode)
                .getResultList();
    }

    @Override
    public Class<ZaoJiCMSStore> getModelClass() {
        return ZaoJiCMSStore.class;
    }
}
