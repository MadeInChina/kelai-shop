package cn.com.kelaikewang.core.inventory.service;

public interface InventoryNotificationService {
    void notification(long productId);
    void retryNotification(long productId);
    //void save(InventoryNotification inventoryNotification);
    void create(long productId,long customerId);
}
