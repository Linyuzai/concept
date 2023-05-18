package com.bytedance.juejin.user.config;

import com.bytedance.juejin.domain.user.UserIdGenerator;
import com.bytedance.juejin.domain.user.UserRepository;
import com.bytedance.juejin.user.domain.user.*;
import com.bytedance.juejin.user.infrastructure.user.mbp.MBPUserIdGenerator;
import com.bytedance.juejin.user.infrastructure.user.mbp.MBPUserRepository;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class DomainUserConfiguration {

    @Bean
    @ConditionalOnMissingBean
    public UserController userController() {
        return new UserController();
    }

    @Bean
    @ConditionalOnMissingBean
    public UserFacadeAdapter userFacadeAdapter() {
        return new UserFacadeAdapterImpl();
    }

    @Bean
    @ConditionalOnMissingBean
    public UserSearcher userSearcher() {
        return new UserSearcherImpl();
    }

    /**
     * MyBatis-Plus 配置
     */
    @Configuration
    public static class MyBatisPlusConfiguration {

        @Bean
        @ConditionalOnMissingBean
        public UserIdGenerator userIdGenerator() {
            return new MBPUserIdGenerator();
        }

        @Bean
        @ConditionalOnMissingBean
        public UserRepository userRepository() {
            return new MBPUserRepository();
        }
    }
}
