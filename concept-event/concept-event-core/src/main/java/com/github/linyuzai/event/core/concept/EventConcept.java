package com.github.linyuzai.event.core.concept;

import com.github.linyuzai.event.core.error.EventErrorHandler;
import com.github.linyuzai.event.core.publisher.EventPublisher;
import com.github.linyuzai.event.core.engine.EventEngine;
import com.github.linyuzai.event.core.exchange.EventExchange;
import com.github.linyuzai.event.core.subscriber.EventSubscriber;

import java.util.Arrays;
import java.util.Collection;

public interface EventConcept {

    EventBuilder event();

    EventBuilder event(Object event);

    Collection<EventEngine> getEngines();

    default void add(EventEngine... engines) {
        add(Arrays.asList(engines));
    }

    void add(Collection<? extends EventEngine> engines);
}
