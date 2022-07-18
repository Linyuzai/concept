package com.github.linyuzai.event.core.endpoint;

import com.github.linyuzai.event.core.error.EventErrorHandler;
import com.github.linyuzai.event.core.publisher.EventPublisher;
import com.github.linyuzai.event.core.engine.EventEngine;
import com.github.linyuzai.event.core.subscriber.EventSubscriber;

import java.util.Map;

public interface EventEndpoint {

    String getName();

    Map<Object, Object> getMetadata();

    void setMetadata(Map<Object, Object> metadata);

    EventEngine getEngine();

    void setEngine(EventEngine publisher);

    EventPublisher getPublisher();

    void setPublisher(EventPublisher publisher);

    EventSubscriber getSubscriber();

    void setSubscriber(EventSubscriber subscriber);

    void publish(Object event);

    void publish(Object event, EventPublisher publisher);

    void publish(Object event, EventErrorHandler errorHandler);

    void publish(Object event, EventPublisher publisher, EventErrorHandler errorHandler);

    void subscribe(EventSubscriber subscriber);
}
