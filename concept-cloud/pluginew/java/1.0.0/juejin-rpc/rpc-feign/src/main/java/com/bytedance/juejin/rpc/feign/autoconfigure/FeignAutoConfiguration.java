package com.bytedance.juejin.rpc.feign.autoconfigure;

import com.bytedance.juejin.login.LoginContext;
import com.bytedance.juejin.rpc.feign.user.FeignUserController;
import com.bytedance.juejin.rpc.feign.user.FeignUserRepository;
import com.bytedance.juejin.domain.user.UserRepository;
import com.bytedance.juejin.token.TokenWebInterceptor;
import feign.RequestInterceptor;
import feign.RequestTemplate;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@EnableFeignClients(basePackages = "com.bytedance.juejin.rpc.feign.*")
public class FeignAutoConfiguration {

    @Bean
    @ConditionalOnMissingBean
    public UserRepository userRepository() {
        return new FeignUserRepository();
    }

    @Bean
    public FeignUserController feignUserController() {
        return new FeignUserController();
    }

    @Bean
    public TokenRequestInterceptor tokenRequestInterceptor() {
        return new TokenRequestInterceptor();
    }

    public static class TokenRequestInterceptor implements RequestInterceptor {

        @Override
        public void apply(RequestTemplate template) {
            System.out.println(LoginContext.getToken());
            template.header(TokenWebInterceptor.TOKEN_HEADER, LoginContext.getToken());
        }
    }
}
