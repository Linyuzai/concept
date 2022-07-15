package com.github.linyuzai.event.core.concept;

import com.github.linyuzai.event.core.publisher.EventPublisher;
import com.github.linyuzai.event.core.engine.EventPublishEngine;
import com.github.linyuzai.event.core.exchange.EventExchange;
import com.github.linyuzai.event.core.subscriber.EventSubscriber;
import lombok.Getter;
import lombok.NonNull;
import lombok.Setter;

import java.util.Collection;
import java.util.Collections;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class DefaultEventConcept implements EventConcept {

    private final Map<String, EventPublishEngine> engineMap = new ConcurrentHashMap<>();

    @Getter
    @Setter
    @NonNull
    private EventExchange exchange = EventExchange.ALL;

    @Override
    public void publish(Object event) {
        publish(event, exchange, null);
    }

    @Override
    public void publish(Object event, EventExchange exchange) {
        publish(event, exchange, null);
    }

    @Override
    public void publish(Object event, EventPublisher handler) {
        publish(event, exchange, handler);
    }

    @Override
    public void publish(Object event, EventExchange exchange, EventPublisher publisher) {
        Collection<EventPublishEngine> engines = exchange.exchangeEngines(getEngines());
        for (EventPublishEngine engine : engines) {
            engine.publish(event, exchange, publisher);
        }
    }

    @Override
    public void subscribe(EventSubscriber subscriber) {
        subscribe(exchange, subscriber);
    }

    @Override
    public void subscribe(EventExchange exchange, EventSubscriber subscriber) {
        Collection<EventPublishEngine> engines = exchange.exchangeEngines(getEngines());
        for (EventPublishEngine engine : engines) {
            engine.subscribe(exchange, subscriber);
        }
    }

    @Override
    public Collection<EventPublishEngine> getEngines() {
        return Collections.unmodifiableCollection(engineMap.values());
    }

    @Override
    public void add(Collection<? extends EventPublishEngine> engines) {
        for (EventPublishEngine engine : engines) {
            this.engineMap.put(engine.getName(), engine);
        }
    }
}
