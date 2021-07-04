package org.broadleafcommerce.payment.service.gateway;

import org.broadleafcommerce.common.config.service.SystemPropertiesService;
import org.broadleafcommerce.common.payment.PaymentGatewayType;
import org.broadleafcommerce.common.payment.service.AbstractPaymentGatewayConfiguration;
import org.broadleafcommerce.vendor.alipay.service.payment.AlipayPaymentGatewayType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service("blAlipayConfiguration")
public class AlipayConfigurationImpl extends AbstractPaymentGatewayConfiguration implements AlipayConfiguration {
    protected int failureReportingThreshold = 1;

    protected boolean performAuthorizeAndCapture = true;

    @Autowired
    protected SystemPropertiesService propertiesService;

    @Override
    public String getAppId() {
        return propertiesService.resolveSystemProperty("gateway.alipay.appId");
    }

    @Override
    public String getMerchantPrivateKey() {
        return propertiesService.resolveSystemProperty("gateway.alipay.merchantPrivateKey");
    }

    @Override
    public String getAlipayPublicKey() {
        return propertiesService.resolveSystemProperty("gateway.alipay.alipayPublicKey");
    }

    @Override
    public String getGateway() {
        return propertiesService.resolveSystemProperty("gateway.alipay.gateway");
    }

    @Override
    public String getNotifyUrl() {
        return propertiesService.resolveSystemProperty("gateway.alipay.notifyUrl");
    }

    @Override
    public String getReturnUrl() {
        return propertiesService.resolveSystemProperty("gateway.alipay.returnUrl");
    }

    @Override
    public boolean handlesAuthorize() {
        return true;
    }

    @Override
    public boolean handlesCapture() {
        return true;
    }

    @Override
    public boolean handlesAuthorizeAndCapture() {
        return true;
    }

    @Override
    public boolean handlesReverseAuthorize() {
        return true;
    }

    @Override
    public boolean handlesVoid() {
        return true;
    }

    @Override
    public boolean handlesRefund() {
        return true;
    }

    @Override
    public boolean handlesPartialCapture() {
        return false;
    }

    @Override
    public boolean handlesMultipleShipment() {
        return false;
    }

    @Override
    public boolean handlesRecurringPayment() {
        return false;
    }

    @Override
    public boolean handlesSavedCustomerPayment() {
        return false;
    }

    @Override
    public boolean isPerformAuthorizeAndCapture() {
        return performAuthorizeAndCapture;
    }

    @Override
    public void setPerformAuthorizeAndCapture(boolean performAuthorizeAndCapture) {
        this.performAuthorizeAndCapture = performAuthorizeAndCapture;
    }

    @Override
    public int getFailureReportingThreshold() {
        return failureReportingThreshold;
    }

    @Override
    public void setFailureReportingThreshold(int failureReportingThreshold) {
        this.failureReportingThreshold = failureReportingThreshold;
    }

    @Override
    public boolean handlesMultiplePayments() {
        return false;
    }

    @Override
    public PaymentGatewayType getGatewayType() {
        return AlipayPaymentGatewayType.ALIPAY;
    }
}
