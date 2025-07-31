package com.saas.platform.config;

import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import io.swagger.v3.oas.models.security.SecurityScheme;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Swagger API 文档配置
 * 
 * @author SaaS Xbox Team
 * @since 2024-07-31
 */
@Configuration
public class SwaggerConfig {

    private static final String SECURITY_SCHEME_NAME = "Authorization";

    @Bean
    public OpenAPI customOpenAPI() {
        return new OpenAPI()
                .info(apiInfo())
                .addSecurityItem(new SecurityRequirement().addList(SECURITY_SCHEME_NAME))
                .components(new Components()
                        .addSecuritySchemes(SECURITY_SCHEME_NAME,
                                new SecurityScheme()
                                        .name(SECURITY_SCHEME_NAME)
                                        .type(SecurityScheme.Type.HTTP)
                                        .scheme("bearer")
                                        .bearerFormat("JWT")
                                        .description("JWT 认证")
                        )
                );
    }

    private Info apiInfo() {
        return new Info()
                .title("SaaS Xbox Backend API")
                .description("SaaS Xbox 管理平台后端接口文档")
                .version("1.0.0")
                .contact(new Contact()
                        .name("SaaS Xbox Team")
                        .email("support@example.com")
                        .url("https://example.com"))
                .license(new License()
                        .name("MIT License")
                        .url("https://opensource.org/licenses/MIT"));
    }
}