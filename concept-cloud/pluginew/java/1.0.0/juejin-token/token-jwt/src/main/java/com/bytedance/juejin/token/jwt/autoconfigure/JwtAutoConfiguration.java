package com.bytedance.juejin.token.jwt.autoconfigure;

import com.bytedance.juejin.token.TokenCodec;
import com.bytedance.juejin.token.jwt.JwtTokenCodec;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class JwtAutoConfiguration {

    @Bean
    @ConditionalOnMissingBean
    public TokenCodec tokenCodec() {
        return new JwtTokenCodec();
    }
}
