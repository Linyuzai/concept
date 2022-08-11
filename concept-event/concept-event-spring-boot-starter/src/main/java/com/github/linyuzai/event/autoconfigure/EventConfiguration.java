package com.github.linyuzai.event.autoconfigure;

import com.github.linyuzai.event.core.codec.EventDecoder;
import com.github.linyuzai.event.core.codec.EventEncoder;
import com.github.linyuzai.event.core.concept.EventConcept;
import com.github.linyuzai.event.core.concept.EventConceptImpl;
import com.github.linyuzai.event.core.context.EventContextFactory;
import com.github.linyuzai.event.core.context.MapEventContextFactory;
import com.github.linyuzai.event.core.engine.EventEngine;
import com.github.linyuzai.event.core.error.EventErrorHandler;
import com.github.linyuzai.event.core.error.LoggerEventErrorHandler;
import com.github.linyuzai.event.core.exchange.EventExchange;
import com.github.linyuzai.event.core.lifecycle.EventConceptLifecycleListener;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.ObjectProvider;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.List;

/**
 * 事件配置类
 */
@Configuration(proxyBeanMethods = false)
public class EventConfiguration {

    @Bean
    @ConditionalOnMissingBean
    public EventContextFactory eventContextFactory() {
        return new MapEventContextFactory();
    }

    @Bean
    @ConditionalOnMissingBean
    public EventErrorHandler eventErrorHandler() {
        Log log = LogFactory.getLog(LoggerEventErrorHandler.class);
        return new LoggerEventErrorHandler(log::error);
    }

    @Bean(initMethod = "initialize", destroyMethod = "destroy")
    @ConditionalOnMissingBean
    public EventConcept eventConcept(EventContextFactory contextFactory,
                                     ObjectProvider<EventExchange> exchangeProvider,
                                     ObjectProvider<EventEncoder> encoderProvider,
                                     ObjectProvider<EventDecoder> decoderProvider,
                                     EventErrorHandler errorHandler,
                                     List<EventEngine> engines,
                                     List<EventConceptLifecycleListener> lifecycleListeners) {
        EventConceptImpl concept = new EventConceptImpl();
        concept.setContextFactory(contextFactory);
        concept.setExchange(exchangeProvider.getIfUnique());
        concept.setEncoder(encoderProvider.getIfUnique());
        concept.setDecoder(decoderProvider.getIfUnique());
        concept.setErrorHandler(errorHandler);
        concept.addEngines(engines);
        concept.addLifecycleListeners(lifecycleListeners);
        return concept;
    }
}
