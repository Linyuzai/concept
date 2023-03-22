package $PACKAGE$.basic.autoconfigure.openapi;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * basic 模块的配置通过 spring.factories 注入
 *
 * 结合 {@link @ConditionalOnMissingBean }进行扩展
 */
@Configuration
public class SpringDocAutoConfiguration {

    @Bean
    public OpenAPI openAPI() {
        return new OpenAPI()
                .info(new Info()
                        .title("$CLASS$ API")
                        .version("$VERSION$"));
    }
}
