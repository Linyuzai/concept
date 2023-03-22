package $PACKAGE$.user.config;

import org.springdoc.core.GroupedOpenApi;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Spring Doc 相关配置
 */
@Configuration
public class UserSpringDocConfiguration {

    @Bean
    public GroupedOpenApi userOpenApi() {
        return GroupedOpenApi.builder()
                .group("用户")
                .packagesToScan("$PACKAGE$.user")
                .build();
    }
}
