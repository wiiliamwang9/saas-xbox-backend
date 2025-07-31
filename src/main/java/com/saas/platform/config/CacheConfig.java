package com.saas.platform.config;

import com.github.benmanes.caffeine.cache.Caffeine;
import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.cache.caffeine.CaffeineCacheManager;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.concurrent.TimeUnit;

/**
 * 缓存配置
 * 
 * @author SaaS Xbox Team
 * @since 2024-07-31
 */
@Configuration
@EnableCaching
public class CacheConfig {

    /**
     * Caffeine 缓存管理器
     */
    @Bean
    public CacheManager cacheManager() {
        CaffeineCacheManager cacheManager = new CaffeineCacheManager();
        
        // 设置缓存规格
        cacheManager.setCaffeine(Caffeine.newBuilder()
                .maximumSize(1000) // 最大缓存数量
                .expireAfterWrite(1, TimeUnit.HOURS) // 写入后1小时过期
                .expireAfterAccess(30, TimeUnit.MINUTES) // 访问后30分钟过期
                .recordStats() // 记录统计信息
        );
        
        return cacheManager;
    }
}