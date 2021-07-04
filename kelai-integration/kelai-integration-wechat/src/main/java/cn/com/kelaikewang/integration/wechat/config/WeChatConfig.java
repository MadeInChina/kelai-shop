package cn.com.kelaikewang.integration.wechat.config;

import cn.com.kelaikewang.integration.wechat.filter.WeChatAuthFilter;
import me.chanjar.weixin.mp.api.WxMpInMemoryConfigStorage;
import me.chanjar.weixin.mp.api.WxMpService;
import org.broadleafcommerce.common.logging.LifeCycleEvent;
import org.broadleafcommerce.common.logging.ModuleLifecycleLoggingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Component;

@Configuration("blWechatModuleConfig")
@Component
public class WeChatConfig {

    @Autowired
    private WeChatConfigProperties weChatConfigProperties;

    @Bean
    public static ModuleLifecycleLoggingBean blWechatLifecycleBean() {
        return new ModuleLifecycleLoggingBean(WechatModuleRegistration.MODULE_NAME, LifeCycleEvent.LOADING);
    }

    @Bean
    public FilterRegistrationBean<WeChatAuthFilter> filterRegistrationBean(@Qualifier("wxMpService") WxMpService wxMpService){
        FilterRegistrationBean<WeChatAuthFilter> bean = new FilterRegistrationBean<>();
        WeChatAuthFilter weChatAuthFilter = new WeChatAuthFilter();
        weChatAuthFilter.setWxMpService(wxMpService);
        bean.setFilter(weChatAuthFilter);
        bean.addUrlPatterns("/*");
        return bean;
    }
    @Bean(name = "wxMpService")
    public WxMpService wxMpService(){
        WxMpService wxMpSrevice = null;
        WxMpInMemoryConfigStorage configStorage = null;

        wxMpSrevice = new me.chanjar.weixin.mp.api.impl.WxMpServiceImpl();
        configStorage = new WxMpInMemoryConfigStorage();
        configStorage.setAppId(weChatConfigProperties.getAppId());
        configStorage.setSecret(weChatConfigProperties.getAppSecret());
        configStorage.setToken(weChatConfigProperties.getToken());
        configStorage.setAesKey(weChatConfigProperties.getEncodingAESKey());
        wxMpSrevice.setWxMpConfigStorage(configStorage);
        return wxMpSrevice;
    }
}
