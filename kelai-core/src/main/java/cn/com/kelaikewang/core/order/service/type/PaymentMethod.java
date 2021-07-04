package cn.com.kelaikewang.core.order.service.type;

import org.broadleafcommerce.common.BroadleafEnumerationType;

import java.io.Serializable;
import java.util.LinkedHashMap;

public class PaymentMethod implements Serializable, BroadleafEnumerationType {

    private static final LinkedHashMap<String, PaymentMethod> TYPES = new LinkedHashMap<>();


    public static final PaymentMethod  PAY_ON_DELIVERY  = new PaymentMethod("PAY_ON_DELIVERY", "货到付款");
    public static final PaymentMethod PAY_ONLINE = new PaymentMethod("PAY_ONLINE", "在线支付");


    public static PaymentMethod getInstance(final String type) {
        return TYPES.get(type);
    }

    private String type;
    private String friendlyType;


    public PaymentMethod() {
        //do nothing
    }

    public PaymentMethod(String type, String friendlyType) {
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

    private void setType(final String type) {
        this.type = type;
        if (!TYPES.containsKey(type)) {
            TYPES.put(type, this);
        }
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
        PaymentMethod other = (PaymentMethod) obj;
        if (type == null) {
            if (other.type != null)
                return false;
        } else if (!type.equals(other.type))
            return false;
        return true;
    }
}



