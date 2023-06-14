package $PACKAGE$.rpc.feign.autoconfigure;

import $PACKAGE$.domain.sample.SampleRepository;
import $PACKAGE$.domain.user.UserRepository;
import $PACKAGE$.rpc.feign.sample.FeignSampleController;
import $PACKAGE$.rpc.feign.sample.FeignSampleRepository;
import $PACKAGE$.rpc.feign.user.FeignUserController;
import $PACKAGE$.rpc.feign.user.FeignUserRepository;
import $PACKAGE$.token.TokenContext;
import $PACKAGE$.token.TokenWebInterceptor;
import feign.RequestInterceptor;
import feign.RequestTemplate;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@EnableFeignClients(basePackages = "$PACKAGE$.rpc.feign.*")
public class FeignAutoConfiguration {

    @Configuration
    public static class SampleConfiguration {

        @Bean
        @ConditionalOnMissingBean
        public SampleRepository sampleRepository() {
            return new FeignSampleRepository();
        }

        @Bean
        @ConditionalOnMissingBean
        public FeignSampleController feignSampleController() {
            return new FeignSampleController();
        }
    }

    @Configuration
    public static class UserConfiguration {

        @Bean
        @ConditionalOnMissingBean
        public UserRepository userRepository() {
            return new FeignUserRepository();
        }

        @Bean
        @ConditionalOnMissingBean
        public FeignUserController feignUserController() {
            return new FeignUserController();
        }
    }

    @Bean
    public TokenRequestInterceptor tokenRequestInterceptor() {
        return new TokenRequestInterceptor();
    }

    public static class TokenRequestInterceptor implements RequestInterceptor {

        @Override
        public void apply(RequestTemplate template) {
            template.header(TokenWebInterceptor.TOKEN_HEADER, TokenContext.getToken());
        }
    }
}
