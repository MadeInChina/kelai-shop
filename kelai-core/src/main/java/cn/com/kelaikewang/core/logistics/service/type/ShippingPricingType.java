package cn.com.kelaikewang.core.logistics.service.type;

import org.broadleafcommerce.common.BroadleafEnumerationType;

import java.io.Serializable;
import java.util.LinkedHashMap;

public class ShippingPricingType implements Serializable, BroadleafEnumerationType {
    private String type;
    private String friendlyType;

    private static final LinkedHashMap<String, ShippingPricingType> TYPES = new LinkedHashMap<>();

    public static final ShippingPricingType NUMBER = new ShippingPricingType("NUMBER","按件数");
    public static final ShippingPricingType WEIGHT = new ShippingPricingType("WEIGHT","按重量");
    public static final ShippingPricingType VOLUME = new ShippingPricingType("VOLUME","按体积");

    public static ShippingPricingType getInstance(final String type) {
        return TYPES.get(type);
    }
    public ShippingPricingType(){

    }

    public ShippingPricingType(String type, String friendlyType) {
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
        ShippingPricingType other = (ShippingPricingType) obj;
        if (type == null) {
            if (other.type != null)
                return false;
        } else if (!type.equals(other.type))
            return false;
        return true;
    }
}
