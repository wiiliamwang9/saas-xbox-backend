package com.saas.platform.config;

import com.baomidou.mybatisplus.annotation.DbType;
import com.baomidou.mybatisplus.core.handlers.MetaObjectHandler;
import com.baomidou.mybatisplus.extension.plugins.MybatisPlusInterceptor;
import com.baomidou.mybatisplus.extension.plugins.inner.PaginationInnerInterceptor;
import org.apache.ibatis.reflection.MetaObject;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.time.LocalDateTime;

/**
 * MyBatis Plus 配置类
 * 
 * @author SaaS Xbox Team
 * @since 2024-07-31
 */
@Configuration
@MapperScan("com.saas.platform.mapper")
public class MyBatisPlusConfig {

    /**
     * MyBatis Plus 拦截器配置
     * 主要用于分页插件
     */
    @Bean
    public MybatisPlusInterceptor mybatisPlusInterceptor() {
        MybatisPlusInterceptor interceptor = new MybatisPlusInterceptor();
        
        // 分页插件
        PaginationInnerInterceptor paginationInnerInterceptor = new PaginationInnerInterceptor();
        paginationInnerInterceptor.setDbType(DbType.MYSQL);
        paginationInnerInterceptor.setMaxLimit(500L); // 单页最大数量限制
        paginationInnerInterceptor.setOverflow(false); // 溢出总页数后是否进行处理
        
        interceptor.addInnerInterceptor(paginationInnerInterceptor);
        
        return interceptor;
    }

    /**
     * 自动填充配置
     * 自动填充创建时间、更新时间等字段
     */
    @Bean
    public MetaObjectHandler metaObjectHandler() {
        return new MetaObjectHandler() {
            @Override
            public void insertFill(MetaObject metaObject) {
                // 创建时间自动填充
                this.strictInsertFill(metaObject, "createdAt", LocalDateTime.class, LocalDateTime.now());
                this.strictInsertFill(metaObject, "updatedAt", LocalDateTime.class, LocalDateTime.now());
            }

            @Override
            public void updateFill(MetaObject metaObject) {
                // 更新时间自动填充
                this.strictUpdateFill(metaObject, "updatedAt", LocalDateTime.class, LocalDateTime.now());
            }
        };
    }
}