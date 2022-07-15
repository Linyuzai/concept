package com.github.linyuzai.event.core.engine;

import com.github.linyuzai.event.core.endpoint.EventPublishEndpoint;
import com.github.linyuzai.event.core.publisher.EventPublisher;
import com.github.linyuzai.event.core.exchange.EventExchange;
import com.github.linyuzai.event.core.subscriber.EventSubscriber;
import lombok.*;

import java.util.Collection;
import java.util.Collections;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Getter
@Setter
@RequiredArgsConstructor
public abstract class AbstractEventPublishEngine implements EventPublishEngine {

    @NonNull
    private String name;

    private Map<Object, Object> metadata;

    private EventPublisher handler;

    private final Map<String, EventPublishEndpoint> endpointMap = new ConcurrentHashMap<>();

    @Override
    public void publish(Object event, EventExchange exchange, EventPublisher publisher) {
        Collection<EventPublishEndpoint> endpoints = exchange.exchangeEndpoints(getEndpoints());
        EventPublisher publisherToUse = publisher == null ? getHandler() : publisher;
        for (EventPublishEndpoint endpoint : endpoints) {
            endpoint.publish(event, publisherToUse);
        }
    }

    @Override
    public void subscribe(EventExchange exchange, EventSubscriber subscriber) {
        Collection<EventPublishEndpoint> endpoints = exchange.exchangeEndpoints(getEndpoints());
        for (EventPublishEndpoint endpoint : endpoints) {
            endpoint.subscribe(subscriber);
        }
    }

    @Override
    public void add(Collection<? extends EventPublishEndpoint> endpoints) {
        for (EventPublishEndpoint endpoint : endpoints) {
            this.endpointMap.put(endpoint.getName(), endpoint);
        }
    }

    public Collection<EventPublishEndpoint> getEndpoints() {
        return Collections.unmodifiableCollection(endpointMap.values());
    }
}
