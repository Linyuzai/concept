package com.github.linyuzai.event.autoconfigure.bus;

import com.github.linyuzai.event.core.bus.EventBus;
import com.github.linyuzai.event.core.concept.EventConcept;
import com.github.linyuzai.event.core.template.EventTemplate;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@EnableConfigurationProperties(EventBusProperties.class)
public class EventBusConfiguration {

    @Bean(initMethod = "initialize", destroyMethod = "destroy")
    @ConditionalOnProperty(name = "concept.event.bus.enabled", havingValue = "true")
    @ConditionalOnMissingBean
    public EventBus eventBus(ApplicationEventPublisher publisher,
                             EventConcept concept,
                             EventBusConfigurer configurer) {
        EventTemplate template = concept.template();
        configurer.configure(template);
        ApplicationEventBus bus = new ApplicationEventBus(publisher);
        bus.setTemplate(template);
        return bus;
    }
}
