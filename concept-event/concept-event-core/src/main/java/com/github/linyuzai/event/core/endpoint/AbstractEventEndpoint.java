package com.github.linyuzai.event.core.endpoint;

import com.github.linyuzai.event.core.codec.EventDecoder;
import com.github.linyuzai.event.core.codec.EventEncoder;
import com.github.linyuzai.event.core.context.EventContext;
import com.github.linyuzai.event.core.error.EventErrorHandler;
import com.github.linyuzai.event.core.exception.EventException;
import com.github.linyuzai.event.core.publisher.EventPublisher;
import com.github.linyuzai.event.core.engine.EventEngine;
import com.github.linyuzai.event.core.subscriber.EventSubscriber;
import lombok.Getter;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

import java.lang.reflect.Type;
import java.util.Map;

@Getter
@Setter
@RequiredArgsConstructor
public abstract class AbstractEventEndpoint implements EventEndpoint {

    @NonNull
    private String name;

    private Map<Object, Object> metadata;

    private EventEngine engine;

    private EventEncoder encoder;

    private EventDecoder decoder;

    private EventErrorHandler errorHandler;

    private EventPublisher publisher;

    private EventSubscriber subscriber;

    @Override
    public void publish(Object event, EventContext context) {
        EventErrorHandler errorHandler = context.get(EventErrorHandler.class);
        try {
            EventEncoder encoder = context.get(EventEncoder.class);
            Object encodedEvent = encoder == null ? event : encoder.encode(event);
            EventPublisher publisher = context.get(EventPublisher.class);
            if (publisher == null) {
                defaultPublish(encodedEvent, context);
            } else {
                publisher.publish(encodedEvent, this, context);
            }
        } catch (Throwable e) {
            errorHandler.onError(e, this, context);
        }
    }

    public void defaultPublish(Object event, EventContext context) {
        throw new EventException("EventPublisher is null");
    }

    @Override
    public void subscribe(Type type, EventContext context) {
        EventErrorHandler errorHandler = context.get(EventErrorHandler.class);
        try {
            EventSubscriber subscriber = context.get(EventSubscriber.class);
            if (subscriber == null) {
                defaultSubscribe(type, context);
            } else {
                subscriber.subscribe(type, this, context);
            }
        } catch (Throwable e) {
            errorHandler.onError(e, this, context);
        }
    }

    public void defaultSubscribe(Type type, EventContext context) {
        throw new EventException("EventSubscriber is null");
    }
}
