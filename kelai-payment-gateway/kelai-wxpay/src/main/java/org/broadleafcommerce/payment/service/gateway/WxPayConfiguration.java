package org.broadleafcommerce.payment.service.gateway;

import org.broadleafcommerce.common.payment.service.PaymentGatewayConfiguration;

public interface WxPayConfiguration extends PaymentGatewayConfiguration {
    /**
     * 微信公众号或者小程序等的appid
     * @return
     */
    //String getAppId();

    /**
     * 微信支付商户号
     * @return
     */
    String getMchId();

    /**
     * 微信支付商户密钥
     * @return
     */
    String getMchKey();

    /**
     * 服务商模式下的子商户公众账号ID
     * @return
     */
    String getSubAppId();

    /**
     * 服务商模式下的子商户号
     * @return
     */
    String getSubMchId();

    /**
     * p12证书的位置，可以指定绝对路径，也可以指定类路径（以classpath:开头）
     * @return
     */
    String getKeyPath();

    /**
     * 支付成功跳转的url
     * @return
     */
    String getReturnUrl();

    /**
     * 后台通知url
     * @return
     */
    String getNotifyUrl();

    /**
     * logo文件
     * @return
     */
    String getLogoFile();

    //String getSecret();

    //String getToken();

    //String getAesKey();
}
