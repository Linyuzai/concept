package com.github.linyuzai.event.autoconfigure.bus;

import com.github.linyuzai.event.core.bus.AbstractEventBus;
import com.github.linyuzai.event.core.concept.EventConcept;
import com.github.linyuzai.event.core.exchange.EventExchange;
import lombok.Getter;
import org.springframework.context.ApplicationEventPublisher;

@Getter
public class ApplicationEventBus extends AbstractEventBus {

    private final ApplicationEventPublisher publisher;

    public ApplicationEventBus(EventConcept concept, EventExchange exchange, ApplicationEventPublisher publisher) {
        super(concept, exchange);
        this.publisher = publisher;
    }

    @Override
    public void onPublish(Object event) {
        publisher.publishEvent(event);
    }

    @Override
    public void onEvent(Object event) {
        publisher.publishEvent(event);
    }
}
