package org.broadleafcommerce.vendor.wxpay.config;

import org.broadleafcommerce.common.module.BroadleafModuleRegistration;

public class WxPayModuleRegistration implements BroadleafModuleRegistration {
    public static final String MODULE_NAME = "WxPay";
    @Override
    public String getModuleName() {
        return MODULE_NAME;
    }
}
