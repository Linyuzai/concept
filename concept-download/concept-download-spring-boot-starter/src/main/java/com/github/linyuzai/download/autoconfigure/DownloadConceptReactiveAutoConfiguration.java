package com.github.linyuzai.download.autoconfigure;

import com.github.linyuzai.download.autoconfigure.properties.DownloadProperties;
import com.github.linyuzai.download.autoconfigure.source.reactive.WebClientSourceFactory;
import com.github.linyuzai.download.autoconfigure.web.reactive.ReactiveDownloadAdvice;
import com.github.linyuzai.download.autoconfigure.web.reactive.ReactiveDownloadConcept;
import com.github.linyuzai.download.core.concept.DownloadConcept;
import com.github.linyuzai.download.core.context.DownloadContextFactory;
import com.github.linyuzai.download.core.event.DownloadEventPublisher;
import com.github.linyuzai.download.core.handler.DownloadHandler;
import com.github.linyuzai.download.core.load.ReactorSourceLoader;
import com.github.linyuzai.download.core.load.SourceLoader;
import com.github.linyuzai.download.core.logger.DownloadLogger;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnWebApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;

import java.util.List;

/**
 * Reactive 环境配置。
 */
@Configuration(proxyBeanMethods = false)
@ConditionalOnWebApplication(type = ConditionalOnWebApplication.Type.REACTIVE)
public class DownloadConceptReactiveAutoConfiguration {

    /*@Bean
    @ConditionalOnMissingBean
    public SourceLoader sourceLoader() {
        return new ReactorSourceLoader();
    }

    @Bean
    @Order(50)
    @ConditionalOnMissingBean
    public WebClientSourceFactory webClientSourceFactory() {
        return new WebClientSourceFactory();
    }*/


    @Bean
    public DownloadConcept downloadConcept(DownloadContextFactory factory,
                                           List<DownloadHandler> handlers,
                                           DownloadEventPublisher eventPublisher,
                                           DownloadLogger logger) {
        return new ReactiveDownloadConcept(factory, handlers, eventPublisher, logger);
    }

    @Bean
    @Order(Ordered.LOWEST_PRECEDENCE - 100)
    public ReactiveDownloadAdvice reactiveDownloadAdvice(DownloadConcept concept,
                                                         DownloadProperties properties) {
        return new ReactiveDownloadAdvice(concept, properties);
    }
}
