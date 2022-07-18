package com.github.linyuzai.event.autoconfigure;

import com.github.linyuzai.event.core.concept.DefaultEventConcept;
import com.github.linyuzai.event.core.concept.EventConcept;
import com.github.linyuzai.event.core.context.DefaultEventContextFactory;
import com.github.linyuzai.event.core.context.EventContextFactory;
import com.github.linyuzai.event.core.engine.EventEngine;
import com.github.linyuzai.event.core.error.EventErrorHandler;
import com.github.linyuzai.event.core.error.LoggerEventErrorHandler;
import com.github.linyuzai.event.core.exchange.EventExchange;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.List;

@Configuration(proxyBeanMethods = false)
public class EventConfiguration {

    @Bean
    @ConditionalOnMissingBean
    public EventContextFactory eventContextFactory() {
        return new DefaultEventContextFactory();
    }

    @Bean
    @ConditionalOnMissingBean
    public EventExchange eventExchange() {
        return EventExchange.ALL;
    }

    @Bean
    @ConditionalOnMissingBean
    public EventErrorHandler eventErrorHandler() {
        Log log = LogFactory.getLog(LoggerEventErrorHandler.class);
        return new LoggerEventErrorHandler(log::error);
    }

    @Bean
    @ConditionalOnMissingBean
    public EventConcept eventConcept(EventContextFactory contextFactory,
                                     EventExchange exchange,
                                     EventErrorHandler errorHandler,
                                     List<EventEngine> engines) {
        DefaultEventConcept concept = new DefaultEventConcept();
        concept.setContextFactory(contextFactory);
        concept.setExchange(exchange);
        concept.setErrorHandler(errorHandler);
        concept.add(engines);
        return concept;
    }
}