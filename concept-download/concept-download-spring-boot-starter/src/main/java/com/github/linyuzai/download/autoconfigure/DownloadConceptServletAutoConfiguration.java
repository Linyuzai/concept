package com.github.linyuzai.download.autoconfigure;

import com.github.linyuzai.download.autoconfigure.properties.DownloadProperties;
import com.github.linyuzai.download.autoconfigure.web.servlet.ServletDownloadAdvice;
import com.github.linyuzai.download.autoconfigure.web.servlet.ServletDownloadConcept;
import com.github.linyuzai.download.core.concept.DownloadConcept;
import com.github.linyuzai.download.core.context.DownloadContextFactory;
import com.github.linyuzai.download.core.event.DownloadEventPublisher;
import com.github.linyuzai.download.core.handler.DownloadHandler;
import com.github.linyuzai.download.core.load.CompletableFutureSourceLoader;
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
 * Servlet 环境配置。
 */
@Configuration(proxyBeanMethods = false)
@ConditionalOnWebApplication(type = ConditionalOnWebApplication.Type.SERVLET)
public class DownloadConceptServletAutoConfiguration {

    @Bean
    @ConditionalOnMissingBean
    public SourceLoader sourceLoader() {
        return new CompletableFutureSourceLoader();
    }

    @Bean
    public DownloadConcept downloadConcept(DownloadContextFactory factory,
                                           List<DownloadHandler> handlers,
                                           DownloadEventPublisher eventPublisher,
                                           DownloadLogger logger) {
        return new ServletDownloadConcept(factory, handlers, eventPublisher, logger);
    }

    @Bean
    @Order(Ordered.LOWEST_PRECEDENCE - 100)
    public ServletDownloadAdvice servletDownloadAdvice(DownloadConcept concept,
                                                       DownloadProperties properties) {
        return new ServletDownloadAdvice(concept, properties);
    }
}
