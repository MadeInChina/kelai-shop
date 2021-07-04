package org.broadleafcommerce.payment.service.gateway;

import org.broadleafcommerce.common.payment.service.PaymentGatewayConfiguration;

public interface AlipayConfiguration extends PaymentGatewayConfiguration {
    String getAppId();
    String getMerchantPrivateKey();
    String getAlipayPublicKey();
    String getGateway();
    String getNotifyUrl();
    String getReturnUrl();
}
