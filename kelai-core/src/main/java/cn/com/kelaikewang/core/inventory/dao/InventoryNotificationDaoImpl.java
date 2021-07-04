package cn.com.kelaikewang.core.inventory.dao;

import cn.com.kelaikewang.core.inventory.domain.InventoryNotification;
import cn.com.kelaikewang.infrastructure.dao.BaseDaoImpl;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository("zjcmsInventoryNotificationDao")
public class InventoryNotificationDaoImpl extends BaseDaoImpl<InventoryNotification> implements InventoryNotificationDao {

    @Override
    public List<InventoryNotification> readAllUnprocessedByProductId(Long productId) {
        List<InventoryNotification> inventoryNotifications = em.createNamedQuery("BC_READ_ALL_UNPROCESSED_BY_PRODUCT_ID",InventoryNotification.class)
                .setParameter("productId",productId)
                .getResultList();
        return inventoryNotifications;
    }

    @Override
    public List<InventoryNotification> readAllFailByProductIdAndTimesLessThan(Long productId, int times) {
        List<InventoryNotification> inventoryNotifications = em.createNamedQuery("BC_READ_ALL_FAIL_BY_PRODUCT_ID_AND_TIMES_LESS_THAN",InventoryNotification.class)
                .setParameter("productId",productId)
                .setParameter("notificationTimes",times)
                .getResultList();
        return inventoryNotifications;
        //BC_READ_BY_PRODUCT_ID_AND_CUSTOMER_ID_AND_STATUS
    }

    @Override
    public InventoryNotification readByProductIdAndCustomerIdAndStatus(long productId, long customerId, String status) {
        List<InventoryNotification> inventoryNotifications = em.createNamedQuery("BC_READ_BY_PRODUCT_ID_AND_CUSTOMER_ID_AND_STATUS",InventoryNotification.class)
                .setParameter("productId",productId)
                .setParameter("customerId",customerId)
                .setParameter("status",status)
                .setMaxResults(1)
                .getResultList();
        if(inventoryNotifications == null || inventoryNotifications.size() == 0){
            return null;
        }
        return inventoryNotifications.get(0);
    }


    @Override
    public Class<InventoryNotification> getModelClass() {
        return InventoryNotification.class;
    }
}
