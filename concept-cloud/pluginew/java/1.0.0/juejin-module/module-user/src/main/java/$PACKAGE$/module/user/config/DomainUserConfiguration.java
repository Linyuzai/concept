package $PACKAGE$.module.user.config;

import $PACKAGE$.domain.user.UserIdGenerator;
import $PACKAGE$.domain.user.UserRepository;
import $PACKAGE$.module.user.domain.user.*;
import $PACKAGE$.module.user.infrastructure.user.mbp.MBPUserIdGenerator;
import $PACKAGE$.module.user.infrastructure.user.mbp.MBPUserRepository;
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
