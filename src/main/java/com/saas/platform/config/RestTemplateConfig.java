package com.saas.platform.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.client.SimpleClientHttpRequestFactory;
import org.springframework.web.client.RestTemplate;

/**
 * RestTemplate配置类
 * 用于HTTP客户端调用Xbox Controller API
 *
 * @author SaaS Xbox Team
 */
@Configuration
public class RestTemplateConfig {

    @Bean
    public RestTemplate restTemplate() {
        SimpleClientHttpRequestFactory factory = new SimpleClientHttpRequestFactory();
        
        // 设置连接超时时间（毫秒）
        factory.setConnectTimeout(5000);
        
        // 设置读取超时时间（毫秒）
        factory.setReadTimeout(10000);
        
        return new RestTemplate(factory);
    }
}