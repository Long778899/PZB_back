package com.peizhenbao.config;

import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import io.swagger.v3.oas.models.security.SecurityScheme;
import org.springdoc.core.models.GroupedOpenApi;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class SwaggerConfig {

    @Bean
    public GroupedOpenApi frontendApi() {
        return GroupedOpenApi.builder()
                .group("前端（用户端）接口")
                .pathsToMatch("/api/**")
                .pathsToExclude("/api/console/**")
                .build();
    }

    @Bean
    public GroupedOpenApi consoleApi() {
        return GroupedOpenApi.builder()
                .group("后台（管理端）接口")
                .pathsToMatch("/api/console/**")
                .build();
    }

    @Bean
    public OpenAPI customOpenAPI() {
        return new OpenAPI()
                .info(new Info()
                        .title("陪诊宝系统 API 接口文档")
                        .version("1.0.0")
                        .description("动态生成的所有前端对接所需的 RESTful API 文档，包含入参、出参和状态码说明。")
                        .contact(new Contact().name("Backend Developer")))
                // 配置全局 JWT 鉴权功能
                .addSecurityItem(new SecurityRequirement().addList("BearerAuth"))
                .components(new Components()
                        .addSecuritySchemes("BearerAuth",
                                new SecurityScheme()
                                        .name("BearerAuth")
                                        .type(SecurityScheme.Type.HTTP)
                                        .scheme("bearer")
                                        .bearerFormat("JWT")
                                        .description("在这里输入用户登录接口返回的 Token (不需要输入 'Bearer ' 前缀)")));
    }
}
