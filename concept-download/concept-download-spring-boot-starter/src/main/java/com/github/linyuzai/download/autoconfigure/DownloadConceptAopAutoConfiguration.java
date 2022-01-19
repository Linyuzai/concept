package com.github.linyuzai.download.autoconfigure;

import com.github.linyuzai.download.core.aop.advice.DownloadAdvice;
import com.github.linyuzai.download.core.aop.advice.DownloadAdvisor;
import org.springframework.aop.Advisor;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * AOP配置 / Configuration of AOP
 */
@Configuration(proxyBeanMethods = false)
@ConditionalOnClass({DownloadAdvice.class, DownloadAdvisor.class})
public class DownloadConceptAopAutoConfiguration {

    @Bean
    @ConditionalOnClass(Advisor.class)
    public DownloadAdvisor downloadConceptAdvisor() {
        return new DownloadAdvisor();
    }
}
