package com.github.linyuzai.download.autoconfigure;

import com.github.linyuzai.download.aop.advice.DownloadConceptAdvice;
import com.github.linyuzai.download.aop.advice.DownloadConceptAdvisor;
import com.github.linyuzai.download.core.concept.DownloadConcept;
import org.springframework.boot.autoconfigure.AutoConfigureAfter;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@AutoConfigureAfter(DownloadConceptAutoConfiguration.class)
@ConditionalOnClass({DownloadConceptAdvice.class, DownloadConceptAdvisor.class})
public class DownloadConceptAopAutoConfiguration {

    //private static final Log logger = LogFactory.getLog(DownloadConceptAopAutoConfiguration.class);

    @Bean
    public DownloadConceptAdvisor downloadConceptAdvisor(DownloadConcept downloadConcept) {
        return new DownloadConceptAdvisor(new DownloadConceptAdvice(downloadConcept));
    }
}
