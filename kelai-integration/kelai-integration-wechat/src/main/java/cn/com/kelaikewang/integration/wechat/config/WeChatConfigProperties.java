package cn.com.kelaikewang.integration.wechat.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

@Configuration
public class WeChatConfigProperties {
    @Value("${wechat.mp.appId}")
    private String appId;
    @Value("${wechat.mp.appSecret}")
    private String appSecret;
    @Value("${wechat.mp.token}")
    private String token;
    @Value("${wechat.mp.encodingAESKey}")
    private String encodingAESKey;

    public String getAppId() {
        return appId;
    }

    public void setAppId(String appId) {
        this.appId = appId;
    }

    public String getAppSecret() {
        return appSecret;
    }

    public void setAppSecret(String appSecret) {
        this.appSecret = appSecret;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public String getEncodingAESKey() {
        return encodingAESKey;
    }

    public void setEncodingAESKey(String encodingAESKey) {
        this.encodingAESKey = encodingAESKey;
    }
}
