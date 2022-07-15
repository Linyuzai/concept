package com.github.linyuzai.event.core.engine;

import com.github.linyuzai.event.core.endpoint.EventPublishEndpoint;
import com.github.linyuzai.event.core.publisher.EventPublisher;
import com.github.linyuzai.event.core.exchange.EventExchange;
import com.github.linyuzai.event.core.subscriber.EventSubscriber;

import java.util.Arrays;
import java.util.Collection;
import java.util.Map;

public interface EventPublishEngine {

    String getName();

    void setName(String name);

    Map<Object, Object> getMetadata();

    void setMetadata(Map<Object, Object> metadata);

    void publish(Object event, EventExchange locator, EventPublisher publisher);

    void subscribe(EventExchange exchange, EventSubscriber subscriber);

    Collection<EventPublishEndpoint> getEndpoints();

    default void add(EventPublishEndpoint... endpoints) {
        add(Arrays.asList(endpoints));
    }

    void add(Collection<? extends EventPublishEndpoint> endpoints);
}
