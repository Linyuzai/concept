package com.github.linyuzai.event.core.engine;

import com.github.linyuzai.event.core.codec.EventDecoder;
import com.github.linyuzai.event.core.codec.EventEncoder;
import com.github.linyuzai.event.core.endpoint.EventEndpoint;
import com.github.linyuzai.event.core.error.EventErrorHandler;
import com.github.linyuzai.event.core.publisher.EventPublisher;
import com.github.linyuzai.event.core.subscriber.EventSubscriber;

import java.util.Arrays;
import java.util.Collection;
import java.util.Map;

public interface EventEngine {

    String getName();

    Map<Object, Object> getMetadata();

    void setMetadata(Map<Object, Object> metadata);

    EventEncoder getEncoder();

    void setEncoder(EventEncoder encoder);

    EventDecoder getDecoder();

    void setDecoder(EventDecoder decoder);

    EventErrorHandler getErrorHandler();

    void setErrorHandler(EventErrorHandler errorHandler);

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
