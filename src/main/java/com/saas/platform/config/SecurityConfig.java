package com.saas.platform.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.web.SecurityFilterChain;

/**
 * Spring Security 配置
 * 
 * @author SaaS Xbox Team
 * @since 2024-07-31
 */
@Configuration
@EnableWebSecurity
public class SecurityConfig {

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
            // 禁用CSRF保护
            .csrf(AbstractHttpConfigurer::disable)
            // 禁用CORS
            .cors(AbstractHttpConfigurer::disable)
            // 配置授权规则
            .authorizeHttpRequests(authz -> authz
                // 允许所有请求访问（暂时完全禁用认证）
                .anyRequest().permitAll()
            )
            // 禁用HTTP Basic认证
            .httpBasic(AbstractHttpConfigurer::disable)
            // 禁用表单登录
            .formLogin(AbstractHttpConfigurer::disable);

        return http.build();
    }
}