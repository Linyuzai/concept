package com.github.linyuzai.event.core.endpoint;

import com.github.linyuzai.event.core.publisher.EventPublisher;
import com.github.linyuzai.event.core.engine.EventPublishEngine;
import com.github.linyuzai.event.core.subscriber.EventSubscriber;

import java.util.Map;

public interface EventPublishEndpoint {

    String getName();

    Map<Object, Object> getMetadata();

    void setMetadata(Map<Object, Object> metadata);

    EventPublishEngine getEngine();

    void setEngine(EventPublishEngine publisher);

    void publish(Object event, EventPublisher publisher);

    void subscribe(EventSubscriber subscriber);
}
