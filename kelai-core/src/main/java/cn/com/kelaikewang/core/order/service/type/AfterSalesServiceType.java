package cn.com.kelaikewang.core.order.service.type;

import org.broadleafcommerce.common.BroadleafEnumerationType;

import java.io.Serializable;
import java.util.LinkedHashMap;

public class AfterSalesServiceType implements Serializable, BroadleafEnumerationType {

    private static final LinkedHashMap<String, AfterSalesServiceType> TYPES = new LinkedHashMap<>();


    public static final AfterSalesServiceType RETURN_REFUND = new AfterSalesServiceType("RETURN_REFUND", "退货退款");
    public static final AfterSalesServiceType REFUND = new AfterSalesServiceType("REFUND", "退款（无需退货）");


    public static AfterSalesServiceType getInstance(final String type) {
        return TYPES.get(type);
    }

    private String type;
    private String friendlyType;


    public AfterSalesServiceType() {
        //do nothing
    }

    public AfterSalesServiceType(String type, String friendlyType) {
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
        AfterSalesServiceType other = (AfterSalesServiceType) obj;
        if (type == null) {
            if (other.type != null)
                return false;
        } else if (!type.equals(other.type))
            return false;
        return true;
    }
}
