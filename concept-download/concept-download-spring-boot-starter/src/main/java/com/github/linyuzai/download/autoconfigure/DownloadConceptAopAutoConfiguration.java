package com.github.linyuzai.download.autoconfigure;

import com.github.linyuzai.download.aop.advice.DownloadConceptAdvice;
import com.github.linyuzai.download.aop.advice.DownloadConceptAdvisor;
import org.springframework.aop.Advisor;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * AOP配置 / Configuration of AOP
 */
@Configuration(proxyBeanMethods = false)
@ConditionalOnClass({DownloadConceptAdvice.class, DownloadConceptAdvisor.class})
public class DownloadConceptAopAutoConfiguration {

    @Bean
    @ConditionalOnClass(Advisor.class)
    public DownloadConceptAdvisor downloadConceptAdvisor() {
        return new DownloadConceptAdvisor();
    }

    /*@Bean
    @ConditionalOnClass(Advisor.class)
    public DownloadAnnotationBeanPostProcessor downloadAnnotationBeanPostProcessor(DownloadConcept downloadConcept) {
        DownloadConceptAdvice advice = new DownloadConceptAdvice(downloadConcept);
        DownloadConceptAdvisor advisor = new DownloadConceptAdvisor(advice);
        return new DownloadAnnotationBeanPostProcessor(advisor);
    }*/
}
