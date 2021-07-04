package org.broadleafcommerce.vendor.wxpay.service.payment;

import java.io.Serializable;

public class CheckOrderStatusResponse implements Serializable {
    //private String returnStatus;
    private String orderStatus;
    private String message;
    private String orderNumber;
    private String unifiedOrderId;


    public String getOrderStatus() {
        return orderStatus;
    }

    public void setOrderStatus(String orderStatus) {
        this.orderStatus = orderStatus;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getOrderNumber() {
        return orderNumber;
    }

    public void setOrderNumber(String orderNumber) {
        this.orderNumber = orderNumber;
    }

    public String getUnifiedOrderId() {
        return unifiedOrderId;
    }

    public void setUnifiedOrderId(String unifiedOrderId) {
        this.unifiedOrderId = unifiedOrderId;
    }
}
