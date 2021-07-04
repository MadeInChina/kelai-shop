package org.broadleafcommerce.payment.web.expression;

import cn.com.kelaikewang.commons.web.RequestUtils;
import eu.bitwalker.useragentutils.DeviceType;
import cn.com.kelaikewang.integration.wechat.config.WeChatConfigProperties;
import org.apache.commons.lang3.StringUtils;
import org.broadleafcommerce.common.web.expression.BroadleafVariableExpression;
import org.broadleafcommerce.payment.service.gateway.WxPayConfiguration;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;

@Component("blWxPayVariableExpression")
public class WxPayVariableExpression implements BroadleafVariableExpression {
    @Resource(name = "blWxPayConfiguration")
    private WxPayConfiguration wxpayConfiguration;
    @Autowired
    private WeChatConfigProperties weChatConfigProperties;
    @Override
    public String getName() {
        return "wxpay";
    }
    public boolean hasConfigProperties() {
        return StringUtils.isNotBlank(weChatConfigProperties.getAppId())
                && StringUtils.isNotBlank(wxpayConfiguration.getMchId())
                && StringUtils.isNotBlank(wxpayConfiguration.getMchKey())
                && StringUtils.isNotBlank(wxpayConfiguration.getKeyPath())
                && StringUtils.isNotBlank(wxpayConfiguration.getNotifyUrl())
                && StringUtils.isNotBlank(wxpayConfiguration.getReturnUrl());
    }
    public boolean isMobileOrTabletDevice(){
        DeviceType deviceType = RequestUtils.getDeviceType();
        return deviceType == DeviceType.MOBILE || deviceType == DeviceType.TABLET;
    }
    public boolean isWeChatEnv(){
        return RequestUtils.isWeChatEnv();
    }
}
