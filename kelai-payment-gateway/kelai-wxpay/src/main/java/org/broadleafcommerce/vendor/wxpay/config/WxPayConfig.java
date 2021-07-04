package org.broadleafcommerce.vendor.wxpay.config;

import org.broadleafcommerce.common.logging.LifeCycleEvent;
import org.broadleafcommerce.common.logging.ModuleLifecycleLoggingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration("blWxPayModuleConfig")
public class WxPayConfig {
    @Bean
    public static ModuleLifecycleLoggingBean blWxPayLifecycleBean() {
        return new ModuleLifecycleLoggingBean(WxPayModuleRegistration.MODULE_NAME, LifeCycleEvent.LOADING);
    }
}
