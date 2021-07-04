package org.broadleafcommerce.payment.service.gateway;

import org.broadleafcommerce.common.config.service.SystemPropertiesService;
import org.broadleafcommerce.common.payment.PaymentGatewayType;
import org.broadleafcommerce.common.payment.service.AbstractPaymentGatewayConfiguration;
import org.broadleafcommerce.vendor.wxpay.service.payment.WxPayPaymentGatewayType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service("blWxPayConfiguration")
public class WxPayConfigurationImpl extends AbstractPaymentGatewayConfiguration implements WxPayConfiguration {
    protected int failureReportingThreshold = 1;

    protected boolean performAuthorizeAndCapture = true;

    @Autowired
    protected SystemPropertiesService propertiesService;


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
        return WxPayPaymentGatewayType.WXPAY;
    }

   /* @Override
    public String getAppId() {
        return propertiesService.resolveSystemProperty("gateway.wxpay.appId");
    }*/

    @Override
    public String getMchId() {
        return propertiesService.resolveSystemProperty("gateway.wxpay.mchId");
    }

    @Override
    public String getMchKey() {
        return propertiesService.resolveSystemProperty("gateway.wxpay.mchKey");
    }

    @Override
    public String getSubAppId() {
        return propertiesService.resolveSystemProperty("gateway.wxpay.subAppId");
    }

    @Override
    public String getSubMchId() {
        return propertiesService.resolveSystemProperty("gateway.wxpay.subMchId");
    }

    @Override
    public String getKeyPath() {
        return propertiesService.resolveSystemProperty("gateway.wxpay.keyPath");
    }

    @Override
    public String getReturnUrl() {
        return propertiesService.resolveSystemProperty("gateway.wxpay.returnUrl");
    }

    @Override
    public String getNotifyUrl() {
        return propertiesService.resolveSystemProperty("gateway.wxpay.notifyUrl");
    }

    @Override
    public String getLogoFile() {
        return propertiesService.resolveSystemProperty("gateway.wxpay.logoFile");
    }

    /*@Override
    public String getSecret() {
        return propertiesService.resolveSystemProperty("gateway.wxpay.secret");
    }

    @Override
    public String getToken() {
        return propertiesService.resolveSystemProperty("gateway.wxpay.token");
    }

    @Override
    public String getAesKey() {
        return propertiesService.resolveSystemProperty("gateway.wxpay.aesKey");
    }*/
}
