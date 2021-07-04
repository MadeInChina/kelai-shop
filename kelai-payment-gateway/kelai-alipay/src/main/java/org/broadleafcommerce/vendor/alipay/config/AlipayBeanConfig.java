package org.broadleafcommerce.vendor.alipay.config;

import com.alipay.api.DefaultAlipayClient;
import org.broadleafcommerce.payment.service.gateway.AlipayConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;

@Configuration
@Component
public class AlipayBeanConfig {
    @Resource(name = "blAlipayConfiguration")
    private AlipayConfiguration alipayConfiguration;

    @Bean(name = "blAlipayClient")
    public DefaultAlipayClient alipayClient(){
       return new DefaultAlipayClient(alipayConfiguration.getGateway(),
               alipayConfiguration.getAppId(),
               alipayConfiguration.getMerchantPrivateKey(),
               "json",
               "UTF-8",
               alipayConfiguration.getAlipayPublicKey(),
               "RSA2");
    }
}
