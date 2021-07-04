package cn.com.kelaikewang.integration.redis.config;

import org.broadleafcommerce.common.config.DefaultOrderFrameworkCommonClasspathPropertySource;

public class RedisProperties extends DefaultOrderFrameworkCommonClasspathPropertySource {

    @Override
    public String getClasspathFolder() {
        return "config/bc/redis/";
    }

}
