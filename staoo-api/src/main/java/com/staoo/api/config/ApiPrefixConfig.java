package com.staoo.api.config;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 * API前缀配置类
 * 用于读取配置文件中的API前缀设置
 */
@Component
@ConfigurationProperties(prefix = "server.api")
public class ApiPrefixConfig {
    
    /**
     * API前缀
     */
    private String prefix = "api";

    public String getPrefix() {
        return prefix;
    }

    public void setPrefix(String prefix) {
        this.prefix = prefix;
    }
}