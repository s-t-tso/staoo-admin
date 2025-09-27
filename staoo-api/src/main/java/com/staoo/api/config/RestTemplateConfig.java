package com.staoo.api.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.client.ClientHttpRequestFactory;
import org.springframework.http.client.SimpleClientHttpRequestFactory;
import org.springframework.web.client.RestTemplate;

/**
 * RestTemplate配置类
 * 配置HTTP客户端，用于发送HTTP请求
 */
@Configuration
public class RestTemplateConfig {

    /**
     * 创建RestTemplate实例
     * 配置超时时间、连接池等参数
     */
    @Bean
    public RestTemplate restTemplate(ClientHttpRequestFactory factory) {
        return new RestTemplate(factory);
    }

    /**
     * 创建ClientHttpRequestFactory实例
     * 配置请求超时时间
     */
    @Bean
    public ClientHttpRequestFactory simpleClientHttpRequestFactory() {
        SimpleClientHttpRequestFactory factory = new SimpleClientHttpRequestFactory();
        // 设置连接超时时间（毫秒）
        factory.setConnectTimeout(5000);
        // 设置读取超时时间（毫秒）
        factory.setReadTimeout(10000);
        return factory;
    }
}