package $PACKAGE$.sample.config;

import org.springdoc.core.GroupedOpenApi;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Spring Doc 相关配置
 */
@Configuration
public class SampleSpringDocConfiguration {

    @Bean
    public GroupedOpenApi sampleOpenApi() {
        return GroupedOpenApi.builder()
                .group("示例")
                .packagesToScan("$PACKAGE$.sample")
                .build();
    }
}
