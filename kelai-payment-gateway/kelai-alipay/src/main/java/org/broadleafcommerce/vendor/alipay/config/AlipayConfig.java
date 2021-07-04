package org.broadleafcommerce.vendor.alipay.config;

import org.broadleafcommerce.common.logging.LifeCycleEvent;
import org.broadleafcommerce.common.logging.ModuleLifecycleLoggingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration("blAlipayModuleConfig")
public class AlipayConfig {
    @Bean
    public static ModuleLifecycleLoggingBean blAlipayLifecycleBean() {
        return new ModuleLifecycleLoggingBean(AlipayModuleRegistration.MODULE_NAME, LifeCycleEvent.LOADING);
    }
}
