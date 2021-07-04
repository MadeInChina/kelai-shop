package cn.com.kelaikewang.integration.redis.config;

import org.broadleafcommerce.common.module.BroadleafModuleRegistration;

public class RedisModuleRegistration  implements BroadleafModuleRegistration {
    public static final String MODULE_NAME = "redis";
    @Override
    public String getModuleName() {
        return MODULE_NAME;
    }
}

