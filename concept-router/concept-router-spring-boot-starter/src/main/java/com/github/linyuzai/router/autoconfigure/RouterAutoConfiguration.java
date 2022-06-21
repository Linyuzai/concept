package com.github.linyuzai.router.autoconfigure;

import com.github.linyuzai.router.core.concept.DefaultRouterConcept;
import com.github.linyuzai.router.core.concept.RouterConcept;
import com.github.linyuzai.router.core.event.DefaultRouterEventPublisher;
import com.github.linyuzai.router.core.event.RouterEventListener;
import com.github.linyuzai.router.core.event.RouterEventPublisher;
import com.github.linyuzai.router.core.locator.RouterLocator;
import com.github.linyuzai.router.core.matcher.RouterMatcher;
import com.github.linyuzai.router.core.repository.JacksonLocalRouterRepository;
import com.github.linyuzai.router.core.repository.RouterRepository;
import com.github.linyuzai.router.core.utils.RouterLogger;
import lombok.extern.apachecommons.CommonsLog;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.List;

@CommonsLog
@Configuration(proxyBeanMethods = false)
public class RouterAutoConfiguration {

    @SuppressWarnings("all")
    @Bean(initMethod = "initialize")
    @ConditionalOnMissingBean
    public RouterRepository routerRepository() {
        return new JacksonLocalRouterRepository();
    }

    @Bean
    public RouterLogger routerLogger() {
        return new RouterLogger(log::info, log::error);
    }

    @Bean
    @ConditionalOnMissingBean
    public RouterEventPublisher routerEventPublisher(List<RouterEventListener> listeners) {
        return new DefaultRouterEventPublisher(listeners);
    }

    @Bean
    @ConditionalOnMissingBean
    public RouterConcept routerConcept(RouterRepository repository,
                                       RouterMatcher matcher,
                                       RouterLocator locator,
                                       RouterEventPublisher eventPublisher) {
        return new DefaultRouterConcept(repository, matcher, locator, eventPublisher);
    }
}
