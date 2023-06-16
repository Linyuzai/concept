package $PACKAGE$.login.autoconfigure;

import $PACKAGE$.login.Login;
import $PACKAGE$.login.LoginArgumentAdapter;
import $PACKAGE$.login.LoginAuthorizer;
import $PACKAGE$.login.LoginAuthorizerImpl;
import $PACKAGE$.login.LoginHandlerMethodArgumentResolver;
import $PACKAGE$.login.LoginUserArgumentAdapter;
import $PACKAGE$.login.LoginWebInterceptor;
import $PACKAGE$.token.TokenCodec;
import com.github.linyuzai.cloud.web.core.concept.Request;
import com.github.linyuzai.cloud.web.core.intercept.annotation.BreakIntercept;
import com.github.linyuzai.cloud.web.core.intercept.annotation.OnRequest;
import lombok.AllArgsConstructor;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.lang.NonNull;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.util.List;

@Configuration
public class LoginAutoConfiguration {

    @Order(-1)
    @BreakIntercept
    @OnRequest
    public boolean nonIntercept(Request request) {
        return request.getPath().startsWith("/login/") ||
                request.getPath().startsWith("/register/");
    }

    @Bean
    @ConditionalOnMissingBean
    public LoginAuthorizer loginAuthorizer(TokenCodec tokenCodec) {
        return new LoginAuthorizerImpl(tokenCodec);
    }

    @Bean
    @ConditionalOnMissingBean
    public LoginWebInterceptor loginWebInterceptor() {
        return new LoginWebInterceptor();
    }

    @Bean
    public LoginUserArgumentAdapter loginUserArgumentAdapter() {
        return new LoginUserArgumentAdapter();
    }

    @Configuration
    @AllArgsConstructor
    public static class MvcConfiguration implements WebMvcConfigurer {

        private List<LoginArgumentAdapter> argumentAdapters;

        /**
         * 对于 Controller 上标记 {@link Login} 的参数进行匹配设置
         */
        @Override
        public void addArgumentResolvers(@NonNull List<HandlerMethodArgumentResolver> resolvers) {
            resolvers.add(new LoginHandlerMethodArgumentResolver(argumentAdapters));
        }
    }
}
