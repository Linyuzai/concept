package com.github.linyuzai.event.autoconfigure.bus;

import com.github.linyuzai.event.core.bus.EventBus;
import com.github.linyuzai.event.core.concept.EventConcept;
import com.github.linyuzai.event.core.exchange.EndpointExchange;
import com.github.linyuzai.event.core.exchange.EventExchange;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.List;

@Configuration
@EnableConfigurationProperties(EventBusProperties.class)
public class EventBusConfiguration {

    @Bean(initMethod = "initialize", destroyMethod = "destroy")
    @ConditionalOnProperty(name = "concept.event.bus.enabled", havingValue = "true")
    @ConditionalOnMissingBean
    public EventBus eventBus(EventBusProperties properties,
                             EventConcept concept,
                             ApplicationEventPublisher publisher,
                             List<EventBusConfigurer> configurers) {
        EventExchange exchange = new EndpointExchange(properties.getEngine(), properties.getEndpoint());
        ApplicationEventBus bus = new ApplicationEventBus(concept, exchange, publisher);
        configurers.forEach(it -> it.configure(bus));
        return bus;
    }
}
