package cn.com.kelaikewang.core.inventory.domain;

import org.broadleafcommerce.common.BroadleafEnumerationType;

import java.io.Serializable;
import java.util.LinkedHashMap;
import java.util.Objects;

public class InventoryNotificationStatus implements Serializable, BroadleafEnumerationType {
    private String type;
    private String friendlyType;

    public static final InventoryNotificationStatus UNPROCESSED = new InventoryNotificationStatus("UNPROCESSED","未通知");
    public static final InventoryNotificationStatus SUCCESS = new InventoryNotificationStatus("SUCCESS","通知成功");
    public static final InventoryNotificationStatus FAIL = new InventoryNotificationStatus("FAIL","通知失败");

    private static final LinkedHashMap<String, InventoryNotificationStatus> TYPES = new LinkedHashMap<>();

    public InventoryNotificationStatus() {
        //do nothing
    }
    public static InventoryNotificationStatus getInstance(final String type) {
        return TYPES.get(type);
    }
    public InventoryNotificationStatus(String type, String friendlyType) {
        this.type = type;
        this.friendlyType = friendlyType;
    }

    public void setType(String type) {
        this.type = type;
        if (!TYPES.containsKey(type)) {
            TYPES.put(type, this);
        }
    }

    public void setFriendlyType(String friendlyType) {
        this.friendlyType = friendlyType;
    }

    @Override
    public String getType() {
        return type;
    }

    @Override
    public String getFriendlyType() {
        return friendlyType;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        InventoryNotificationStatus that = (InventoryNotificationStatus) o;
        return Objects.equals(type, that.type) &&
                Objects.equals(friendlyType, that.friendlyType);
    }

    @Override
    public int hashCode() {
        return Objects.hash(type, friendlyType);
    }
}
