package cn.com.kelaikewang.core.inventory.dao;

import cn.com.kelaikewang.core.inventory.domain.InventoryNotification;
import cn.com.kelaikewang.infrastructure.dao.BaseDao;

import java.util.List;

public interface InventoryNotificationDao extends BaseDao<InventoryNotification> {
    List<InventoryNotification> readAllUnprocessedByProductId(Long productId);
    List<InventoryNotification> readAllFailByProductIdAndTimesLessThan(Long productId,int times);
    InventoryNotification readByProductIdAndCustomerIdAndStatus(long productId,long customerId,String status);
}
