package com.bytedance.juejin.pin.config;

import org.springdoc.core.GroupedOpenApi;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Spring Doc 相关配置
 */
@Configuration
public class SpringDocConfiguration {

    @Bean
    public GroupedOpenApi pinOpenApi() {
        return GroupedOpenApi.builder()
                .group("pin")
                .displayName("沸点")
                .packagesToScan("com.bytedance.juejin.pin")
                .build();
    }
}
