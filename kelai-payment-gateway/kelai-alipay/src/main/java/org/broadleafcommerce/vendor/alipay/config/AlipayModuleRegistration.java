package org.broadleafcommerce.vendor.alipay.config;

import org.broadleafcommerce.common.module.BroadleafModuleRegistration;

public class AlipayModuleRegistration implements BroadleafModuleRegistration {
    public static final String MODULE_NAME = "Alipay";
    @Override
    public String getModuleName() {
        return MODULE_NAME;
    }
}
