package org.broadleafcommerce.vendor.wxpay.config;


import com.github.binarywang.wxpay.config.WxPayConfig;
import com.github.binarywang.wxpay.service.WxPayService;
import com.github.binarywang.wxpay.service.impl.WxPayServiceImpl;
import cn.com.kelaikewang.integration.wechat.config.WeChatConfigProperties;
import org.apache.commons.lang3.StringUtils;
import org.broadleafcommerce.payment.service.gateway.WxPayConfiguration;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;

@Configuration
@Component
public class WxPayBeanConfig {
    @Resource(name = "blWxPayConfiguration")
    private WxPayConfiguration wxPayConfiguration;

    @Autowired
    private WeChatConfigProperties weChatConfigProperties;

    @Bean(name = "wxPayService")
    public WxPayService wxPayService(){
        WxPayConfig payConfig = new WxPayConfig();
        payConfig.setAppId(StringUtils.trimToNull(weChatConfigProperties.getAppId()));
        payConfig.setMchId(StringUtils.trimToNull(wxPayConfiguration.getMchId()));
        payConfig.setMchKey(StringUtils.trimToNull(wxPayConfiguration.getMchKey()));
        payConfig.setSubAppId(StringUtils.trimToNull(wxPayConfiguration.getSubAppId()));
        payConfig.setSubMchId(StringUtils.trimToNull(wxPayConfiguration.getSubMchId()));
        payConfig.setKeyPath(StringUtils.trimToNull(wxPayConfiguration.getKeyPath()));

        // 可以指定是否使用沙箱环境
        payConfig.setUseSandboxEnv(false);

        WxPayService wxPayService = new WxPayServiceImpl();
        wxPayService.setConfig(payConfig);
        return wxPayService;
    }

}
