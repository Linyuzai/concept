package com.github.linyuzai.download.autoconfigure;

import com.github.linyuzai.download.aop.advice.DownloadConceptAdvice;
import com.github.linyuzai.download.aop.advice.DownloadConceptAdvisor;
import com.github.linyuzai.download.core.concept.DownloadConcept;
import org.springframework.aop.support.DefaultPointcutAdvisor;
import org.springframework.boot.autoconfigure.AutoConfigureAfter;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * AOP配置 / Configuration of AOP
 */
@Configuration
@AutoConfigureAfter(DownloadConceptAutoConfiguration.class)
@ConditionalOnClass({DownloadConceptAdvice.class, DownloadConceptAdvisor.class})
public class DownloadConceptAopAutoConfiguration {

    @Bean
    @ConditionalOnClass(DefaultPointcutAdvisor.class)
    public DownloadConceptAdvisor downloadConceptAdvisor(DownloadConcept downloadConcept) {
        return new DownloadConceptAdvisor(new DownloadConceptAdvice(downloadConcept));
    }
}
