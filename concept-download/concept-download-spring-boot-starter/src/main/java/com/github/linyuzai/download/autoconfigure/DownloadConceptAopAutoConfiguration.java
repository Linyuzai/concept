package com.github.linyuzai.download.autoconfigure;

import com.github.linyuzai.download.aop.advice.DownloadConceptAdvice;
import com.github.linyuzai.download.aop.advice.DownloadConceptAdvisor;
import com.github.linyuzai.download.core.concept.DownloadConcept;
import org.springframework.boot.autoconfigure.AutoConfigureAfter;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@ConditionalOnClass({DownloadConceptAdvice.class, DownloadConceptAdvisor.class})
@AutoConfigureAfter(DownloadConceptAutoConfiguration.class)
public class DownloadConceptAopAutoConfiguration {

    @Bean
    public DownloadConceptAdvice downloadConceptAdvice(DownloadConcept downloadConcept) {
        return new DownloadConceptAdvice(downloadConcept);
    }

    @Bean
    public DownloadConceptAdvisor downloadConceptAdvisor(DownloadConceptAdvice advice) {
        return new DownloadConceptAdvisor(advice);
    }
}
