package $PACKAGE$.login.autoconfigure;

import org.springdoc.core.GroupedOpenApi;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Spring Doc 相关配置
 */
@Configuration
public class SpringDocConfiguration {

    @Bean
    public GroupedOpenApi loginOpenApi() {
        return GroupedOpenApi.builder()
                .group("login")
                .displayName("登录")
                .packagesToScan("$PACKAGE$.login")
                .build();
    }
}
