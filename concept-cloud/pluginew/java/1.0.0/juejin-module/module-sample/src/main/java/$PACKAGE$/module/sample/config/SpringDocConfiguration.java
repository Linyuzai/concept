package $PACKAGE$.module.sample.config;

import org.springdoc.core.GroupedOpenApi;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Spring Doc 相关配置
 */
@Configuration
public class SpringDocConfiguration {

    @Bean
    public GroupedOpenApi sampleOpenApi() {
        return GroupedOpenApi.builder()
                .group("sample")
                .displayName("示例")
                .packagesToScan("$PACKAGE$.module.sample")
                .build();
    }
}
