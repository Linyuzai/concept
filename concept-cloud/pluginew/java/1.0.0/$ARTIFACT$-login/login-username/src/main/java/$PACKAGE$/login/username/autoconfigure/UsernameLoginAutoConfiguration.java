package $PACKAGE$.login.username.autoconfigure;

import $PACKAGE$.login.username.UsernameLoginController;
import $PACKAGE$.login.username.UsernameRegisterController;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class UsernameLoginAutoConfiguration {

    @Bean
    @ConditionalOnMissingBean
    public UsernameLoginController usernameLoginController() {
        return new UsernameLoginController();
    }

    @Bean
    @ConditionalOnMissingBean
    public UsernameRegisterController usernameRegisterController() {
        return new UsernameRegisterController();
    }
}
