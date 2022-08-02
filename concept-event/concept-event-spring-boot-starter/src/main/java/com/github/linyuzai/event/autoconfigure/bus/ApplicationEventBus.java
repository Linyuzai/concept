package com.github.linyuzai.event.autoconfigure.bus;

import com.github.linyuzai.event.core.bus.AbstractEventBus;
import com.github.linyuzai.event.core.concept.EventConcept;
import com.github.linyuzai.event.core.exchange.EventExchange;
import lombok.Getter;
import org.springframework.context.ApplicationEventPublisher;

@Getter
public class ApplicationEventBus extends AbstractEventBus {

    private final ApplicationEventPublisher eventPublisher;

    public ApplicationEventBus(EventConcept concept, EventExchange exchange, ApplicationEventPublisher eventPublisher) {
        super(concept, exchange);
        this.eventPublisher = eventPublisher;
    }

    @Override
    public void onPublish(Object event) {
        eventPublisher.publishEvent(event);
    }

    @Override
    public void onEvent(Object event) {
        eventPublisher.publishEvent(event);
    }
}
