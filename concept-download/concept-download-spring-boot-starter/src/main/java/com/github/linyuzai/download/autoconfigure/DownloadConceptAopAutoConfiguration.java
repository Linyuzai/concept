package com.github.linyuzai.download.autoconfigure;

import com.github.linyuzai.download.core.aop.advice.DownloadConceptAdvice;
import org.springframework.aop.Advisor;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * AOP配置 / Configuration of AOP
 */
@Configuration(proxyBeanMethods = false)
@ConditionalOnClass(DownloadConceptAdvice.class)
public class DownloadConceptAopAutoConfiguration {

    @Bean
    @ConditionalOnClass(Advisor.class)
    public DownloadConceptAdvice downloadConceptAdvice() {
        return new DownloadConceptAdvice();
    }
}
