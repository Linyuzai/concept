package com.github.linyuzai.event.core.concept;

import com.github.linyuzai.event.core.publisher.EventPublisher;
import com.github.linyuzai.event.core.engine.EventPublishEngine;
import com.github.linyuzai.event.core.exchange.EventExchange;
import com.github.linyuzai.event.core.subscriber.EventSubscriber;

import java.util.Arrays;
import java.util.Collection;

public interface EventConcept {

    void publish(Object event);

    void publish(Object event, EventExchange exchange);

    void publish(Object event, EventPublisher publisher);

    void publish(Object event, EventExchange exchange, EventPublisher publisher);

    void subscribe(EventSubscriber subscriber);

    void subscribe(EventExchange exchange, EventSubscriber subscriber);

    Collection<EventPublishEngine> getEngines();

    default void add(EventPublishEngine... engines) {
        add(Arrays.asList(engines));
    }

    void add(Collection<? extends EventPublishEngine> engines);
}
