package com.github.linyuzai.event.core.engine;

import com.github.linyuzai.event.core.endpoint.EventEndpoint;
import com.github.linyuzai.event.core.publisher.EventPublisher;
import com.github.linyuzai.event.core.subscriber.EventSubscriber;

import java.util.Arrays;
import java.util.Collection;
import java.util.Map;

public interface EventEngine {

    String getName();

    void setName(String name);

    Map<Object, Object> getMetadata();

    void setMetadata(Map<Object, Object> metadata);

    EventPublisher getPublisher();

    void setPublisher(EventPublisher publisher);

    EventSubscriber getSubscriber();

    void setSubscriber(EventSubscriber subscriber);

    Collection<EventEndpoint> getEndpoints();

    default void add(EventEndpoint... endpoints) {
        add(Arrays.asList(endpoints));
    }

    void add(Collection<? extends EventEndpoint> endpoints);
}
