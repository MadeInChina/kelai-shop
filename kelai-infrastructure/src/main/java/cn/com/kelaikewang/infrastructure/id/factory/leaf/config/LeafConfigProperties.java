package cn.com.kelaikewang.infrastructure.id.factory.leaf.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.validation.annotation.Validated;

@Validated
@Component
public class LeafConfigProperties {
    @Value("${leaf.server}")
    private String server;
    @Value("${leaf.generator}")
    private String generator;
    /**
     * 各个业务不同的发号需求用key来区分
     */
    private String key;

    public String getServer() {
        return server;
    }

    public void setServer(String server) {
        this.server = server;
    }

    public String getGenerator() {
        return generator;
    }

    public void setGenerator(String generator) {
        this.generator = generator;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }
}
