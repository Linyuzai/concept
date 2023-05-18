package $PACKAGE$.token.autoconfigure;

import $PACKAGE$.token.TokenWebInterceptor;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class TokenAutoConfiguration {

    @Bean
    @ConditionalOnMissingBean
    public TokenWebInterceptor tokenWebInterceptor() {
        return new TokenWebInterceptor();
    }
}
