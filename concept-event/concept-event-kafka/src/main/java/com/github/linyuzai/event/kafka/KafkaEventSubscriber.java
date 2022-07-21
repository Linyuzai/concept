package com.github.linyuzai.event.kafka;

import com.github.linyuzai.event.core.context.EventContext;
import com.github.linyuzai.event.core.endpoint.EventEndpoint;
import com.github.linyuzai.event.core.subscriber.GenericEventSubscriber;

import java.lang.reflect.Type;

public interface KafkaEventSubscriber<T> extends GenericEventSubscriber<T> {

    @Override
    default void subscribe(Type type, EventEndpoint endpoint, EventContext context) {
        if (endpoint instanceof KafkaEventEndpoint) {
            subscribe(type, (KafkaEventEndpoint) endpoint, context);
        }
    }

    void subscribe(Type type, KafkaEventEndpoint endpoint, EventContext context);

    @Override
    default void onEvent(T event, EventEndpoint endpoint, EventContext context) {
        onEvent(event, (KafkaEventEndpoint) endpoint, context);
    }

    void onEvent(T event, KafkaEventEndpoint endpoint, EventContext context);
}
