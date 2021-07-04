package org.broadleafcommerce.vendor.alipay.service.payment;

public class UnifiedOrderResponse {
    private String status;
    private String message;
    private String form;

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getForm() {
        return form;
    }

    public void setForm(String form) {
        this.form = form;
    }
}
