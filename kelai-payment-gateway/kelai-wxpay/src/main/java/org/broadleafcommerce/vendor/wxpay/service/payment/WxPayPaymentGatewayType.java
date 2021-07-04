package org.broadleafcommerce.vendor.wxpay.service.payment;

import org.broadleafcommerce.common.payment.PaymentGatewayType;

public class WxPayPaymentGatewayType extends PaymentGatewayType {
    public static final PaymentGatewayType WXPAY  = new PaymentGatewayType("WxPay", "微信支付");
}
