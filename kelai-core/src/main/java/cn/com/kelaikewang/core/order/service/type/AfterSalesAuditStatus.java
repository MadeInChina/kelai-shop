package cn.com.kelaikewang.core.order.service.type;

import org.broadleafcommerce.common.BroadleafEnumerationType;

import java.io.Serializable;
import java.util.LinkedHashMap;

public class AfterSalesAuditStatus implements Serializable, BroadleafEnumerationType {

    private static final LinkedHashMap<String, AfterSalesAuditStatus> TYPES = new LinkedHashMap<>();

    public static final AfterSalesAuditStatus AUDITING = new AfterSalesAuditStatus("AUDITING", "商家审核中");
    public static final AfterSalesAuditStatus AGREE = new AfterSalesAuditStatus("AGREE", "同意退款");
    public static final AfterSalesAuditStatus DISAGREE = new AfterSalesAuditStatus("DISAGREE", "拒绝退款");


    public static AfterSalesAuditStatus getInstance(final String type) {
        return TYPES.get(type);
    }

    private String type;
    private String friendlyType;


    public AfterSalesAuditStatus() {
        //do nothing
    }

    public AfterSalesAuditStatus(String type, String friendlyType) {
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
        AfterSalesAuditStatus other = (AfterSalesAuditStatus) obj;
        if (type == null) {
            if (other.type != null)
                return false;
        } else if (!type.equals(other.type))
            return false;
        return true;
    }
}

