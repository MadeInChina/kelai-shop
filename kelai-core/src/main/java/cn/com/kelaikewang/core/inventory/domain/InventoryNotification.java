package cn.com.kelaikewang.core.inventory.domain;

import java.util.Date;

public interface InventoryNotification {
    Long getId();
    void setId(Long id);
    Long getCustomerId();
    void setCustomerId(Long customerId);
    Long getProductId();
    void setProductId(Long id);
    InventoryNotificationStatus getNotificationStatus();
    void setNotificationStatus(InventoryNotificationStatus notificationStatus);
    Date getNotificationDateTime();
    void setNotificationDateTime(Date notificationDateTime);
    Date getCreated();
    void setCreated(Date created);
    Integer getNotificationTimes();
    void setNotificationTimes(Integer notificationTimes);
}
