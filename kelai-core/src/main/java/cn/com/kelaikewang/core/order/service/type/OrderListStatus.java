package cn.com.kelaikewang.core.order.service.type;

import org.broadleafcommerce.common.BroadleafEnumerationType;

import java.io.Serializable;
import java.util.LinkedHashMap;

public class OrderListStatus implements Serializable, BroadleafEnumerationType {

    private static final long serialVersionUID = 1L;

    private static final LinkedHashMap<String, OrderListStatus> TYPES = new LinkedHashMap<>();


    public static final OrderListStatus SUBMITTED = new OrderListStatus("SUBMITTED", "待支付");
    public static final OrderListStatus PAID = new OrderListStatus("PAID", "待发货");
    public static final OrderListStatus SHIPPED = new OrderListStatus("SHIPPED", "待收货");
    public static final OrderListStatus RECEIVED = new OrderListStatus("RECEIVED", "待评价");
    public static final OrderListStatus AFTER_SALES_SERVICE = new OrderListStatus("AFTER_SALES_SERVICE", "售后");


    public static OrderListStatus getInstance(final String type) {
        return TYPES.get(type);
    }

    private String type;
    private String friendlyType;


    public OrderListStatus() {
        //do nothing
    }

    public OrderListStatus(String type, String friendlyType) {
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
        OrderListStatus other = (OrderListStatus) obj;
        if (type == null) {
            if (other.type != null)
                return false;
        } else if (!type.equals(other.type))
            return false;
        return true;
    }
}
