package com.bytedance.juejin.user.config;

import org.springdoc.core.GroupedOpenApi;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Spring Doc 相关配置
 */
@Configuration
public class SpringDocConfiguration {

    @Bean
    public GroupedOpenApi userOpenApi() {
        return GroupedOpenApi.builder()
                .group("user")
                .displayName("用户")
                .packagesToScan("com.bytedance.juejin.user")
                .build();
    }
}
