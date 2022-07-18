package com.github.linyuzai.event.core.endpoint;

import com.github.linyuzai.event.core.error.EventErrorHandler;
import com.github.linyuzai.event.core.publisher.EventPublisher;
import com.github.linyuzai.event.core.engine.EventEngine;
import com.github.linyuzai.event.core.subscriber.EventSubscriber;
import lombok.Getter;
import lombok.Setter;

import java.util.Map;

@Getter
@Setter
public abstract class AbstractEventEndpoint implements EventEndpoint {

    private String name;

    private Map<Object, Object> metadata;

    private EventEngine engine;

    private EventPublisher publisher;

    private EventSubscriber subscriber;

    private EventErrorHandler errorHandler;

    @Override
    public void publish(Object event) {
        publish(event, publisher, errorHandler);
    }

    @Override
    public void publish(Object event, EventPublisher publisher) {
        publish(event, publisher, errorHandler);
    }

    @Override
    public void publish(Object event, EventErrorHandler errorHandler) {
        publish(event, publisher, errorHandler);
    }
}
