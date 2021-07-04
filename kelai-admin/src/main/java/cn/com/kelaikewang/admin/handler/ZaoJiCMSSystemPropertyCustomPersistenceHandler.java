package cn.com.kelaikewang.admin.handler;

import org.broadleafcommerce.common.config.domain.SystemProperty;
import org.broadleafcommerce.common.exception.ServiceException;
import org.broadleafcommerce.openadmin.dto.PersistencePackage;
import org.broadleafcommerce.openadmin.server.dao.DynamicEntityDao;
import org.broadleafcommerce.openadmin.server.service.handler.SystemPropertyCustomPersistenceHandler;
import org.broadleafcommerce.openadmin.server.service.persistence.module.RecordHelper;


public class ZaoJiCMSSystemPropertyCustomPersistenceHandler extends SystemPropertyCustomPersistenceHandler {
    @Override
    public Boolean canHandleRemove(PersistencePackage persistencePackage) {
        String ceilingEntityFullyQualifiedClassname = persistencePackage.getCeilingEntityFullyQualifiedClassname();
        try {
            Class testClass = Class.forName(ceilingEntityFullyQualifiedClassname);
            return SystemProperty.class.isAssignableFrom(testClass);
        } catch (ClassNotFoundException e) {
            return false;
        }
    }

    @Override
    public void remove(PersistencePackage persistencePackage, DynamicEntityDao dynamicEntityDao, RecordHelper helper) throws ServiceException {
        super.remove(persistencePackage, dynamicEntityDao, helper);
    }
}
