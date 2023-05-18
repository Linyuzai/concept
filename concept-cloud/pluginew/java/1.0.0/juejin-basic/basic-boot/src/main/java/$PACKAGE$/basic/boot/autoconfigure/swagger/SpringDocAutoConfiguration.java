package $PACKAGE$.basic.boot.autoconfigure.swagger;

import com.github.linyuzai.cloud.web.core.concept.Request;
import com.github.linyuzai.cloud.web.core.intercept.annotation.BreakIntercept;
import com.github.linyuzai.cloud.web.core.intercept.annotation.OnRequest;
import com.github.linyuzai.cloud.web.core.intercept.annotation.OnResponse;
import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import io.swagger.v3.oas.models.security.SecurityScheme;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpHeaders;

/**
 * 全局 Spring Doc 配置
 */
@Configuration
public class SpringDocAutoConfiguration {

    @Bean
    public OpenAPI openAPI() {
        return new OpenAPI()
                .info(new Info()
                        .title("Juejin API")
                        .version("1.0"))
                .addSecurityItem(new SecurityRequirement()
                        .addList(HttpHeaders.AUTHORIZATION))
                .components(new Components()
                        .addSecuritySchemes(HttpHeaders.AUTHORIZATION, new SecurityScheme()
                                .name(HttpHeaders.AUTHORIZATION)
                                .type(SecurityScheme.Type.HTTP)
                                .scheme("bearer")
                                .bearerFormat("JWT")
                        ));
    }

    /**
     * 不拦截 /api-docs/
     */
    @Order(-1)
    @BreakIntercept
    @OnRequest
    @OnResponse
    public boolean nonIntercept(Request request) {
        return request.getPath().contains("/api-docs/");
    }
}
