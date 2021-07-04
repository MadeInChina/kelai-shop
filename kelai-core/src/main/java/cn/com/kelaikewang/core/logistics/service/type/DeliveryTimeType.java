package cn.com.kelaikewang.core.logistics.service.type;

import org.broadleafcommerce.common.BroadleafEnumerationType;

import java.io.Serializable;
import java.util.LinkedHashMap;

public class DeliveryTimeType implements Serializable, BroadleafEnumerationType {
    private String type;
    private String friendlyType;

    private static final LinkedHashMap<String, DeliveryTimeType> TYPES = new LinkedHashMap<>();

    public static final DeliveryTimeType FOUR_HOURS = new DeliveryTimeType("FOUR_HOURS", "4小时内");
    public static final DeliveryTimeType EIGHT_HOURS = new DeliveryTimeType("EIGHT_HOURS", "8小时内");
    public static final DeliveryTimeType SIXTEEN_HOURS = new DeliveryTimeType("SIXTEEN_HOURS", "16小时内");
    public static final DeliveryTimeType TWENTY_HOURS = new DeliveryTimeType("TWENTY_HOURS", "20小时内");
    public static final DeliveryTimeType ONE_DAY = new DeliveryTimeType("ONE_DAY", "1天内");
    public static final DeliveryTimeType TWO_DAYS = new DeliveryTimeType("TWO_DAYS", "2天内");
    public static final DeliveryTimeType THREE_DAYS = new DeliveryTimeType("THREE_DAYS", "3天内");

    public static final DeliveryTimeType FIVE_DAYS = new DeliveryTimeType("FIVE_DAYS", "5天内");
    public static final DeliveryTimeType SEVEN_DAYS = new DeliveryTimeType("SEVEN_DAYS", "7天内");
    public static final DeliveryTimeType EIGHT_DAYS = new DeliveryTimeType("EIGHT_DAYS", "8天内");
    public static final DeliveryTimeType TEN_DAYS = new DeliveryTimeType("TEN_DAYS", "10天内");
    public static final DeliveryTimeType TWELVE_DAYS = new DeliveryTimeType("TWELVE_DAYS", "12天内");

    public static final DeliveryTimeType FIFTEEN_DAYS = new DeliveryTimeType("FIFTEEN_DAYS", "15天内");
    public static final DeliveryTimeType SEVENTEEN_DAYS = new DeliveryTimeType("SEVENTEEN_DAYS", "17天内");

    public static final DeliveryTimeType TWENTY_DAYS = new DeliveryTimeType("TWENTY_DAYS", "20天内");
    public static final DeliveryTimeType TWENTY_FIVE_DAYS = new DeliveryTimeType("TWENTY_FIVE_DAYS", "25天内");
    public static final DeliveryTimeType THIRTY_DAYS = new DeliveryTimeType("THIRTY_DAYS", "30天内");
    public static final DeliveryTimeType THIRTY_FIVE_DAYS = new DeliveryTimeType("THIRTY_FIVE_DAYS", "35天内");
    public static final DeliveryTimeType FORTY_FIVE_DAYS = new DeliveryTimeType("FORTY_FIVE_DAYS", "45天内");

    public static DeliveryTimeType getInstance(final String type) {
        return TYPES.get(type);
    }

    public DeliveryTimeType() {

    }

    public DeliveryTimeType(String type, String friendlyType) {
        this.friendlyType = friendlyType;
        this.setType(type);
    }

    @Override
    public String getType() {
        return type;
    }

    @Override
    public String getFriendlyType() {
        return friendlyType;
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
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((type == null) ? 0 : type.hashCode());
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (!getClass().isAssignableFrom(obj.getClass()))
            return false;
        DeliveryTimeType other = (DeliveryTimeType) obj;
        if (type == null) {
            if (other.type != null)
                return false;
        } else if (!type.equals(other.type))
            return false;
        return true;
    }
}