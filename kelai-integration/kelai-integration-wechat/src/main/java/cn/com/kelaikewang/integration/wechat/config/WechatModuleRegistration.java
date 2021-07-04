package cn.com.kelaikewang.integration.wechat.config;

import org.broadleafcommerce.common.module.BroadleafModuleRegistration;

public class WechatModuleRegistration implements BroadleafModuleRegistration {
    public static final String MODULE_NAME = "wechat";
    @Override
    public String getModuleName() {
        return MODULE_NAME;
    }
}
