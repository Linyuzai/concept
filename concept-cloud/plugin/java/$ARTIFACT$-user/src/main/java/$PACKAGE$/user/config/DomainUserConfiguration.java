package $PACKAGE$.user.config;

import $PACKAGE$.basic.rpc.api.user.UserApi;
import $PACKAGE$.user.domain.user.*;
import $PACKAGE$.user.domain.user.rpc.InnerUserApi;
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
    public UserApi userApi() {
        return new InnerUserApi();
    }
}
