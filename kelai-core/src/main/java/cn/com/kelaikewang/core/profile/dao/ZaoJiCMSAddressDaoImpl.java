package cn.com.kelaikewang.core.profile.dao;

import cn.com.kelaikewang.core.profile.domain.ZaoJiCMSAddress;
import cn.com.kelaikewang.infrastructure.dao.BaseDaoImpl;
import org.springframework.stereotype.Repository;

@Repository("zjcmsAddressDao")
public class ZaoJiCMSAddressDaoImpl extends BaseDaoImpl<ZaoJiCMSAddress> implements ZaoJiCMSAddressDao {
    @Override
    public Class<ZaoJiCMSAddress> getModelClass() {
        return ZaoJiCMSAddress.class;
    }
}
