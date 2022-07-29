package com.github.linyuzai.event.rabbitmq.subscriber;

import com.github.linyuzai.event.core.context.EventContext;
import com.github.linyuzai.event.core.endpoint.EventEndpoint;
import com.github.linyuzai.event.core.subscriber.GenericEventSubscriber;
import com.github.linyuzai.event.rabbitmq.endpoint.RabbitEventEndpoint;

import java.lang.reflect.Type;

public interface RabbitEventSubscriber<T> extends GenericEventSubscriber<T> {

    @Override
    default void subscribe(Type type, EventEndpoint endpoint, EventContext context) {
        if (endpoint instanceof RabbitEventEndpoint) {
            subscribe(type, (RabbitEventEndpoint) endpoint, context);
        }
    }

    void subscribe(Type type, RabbitEventEndpoint endpoint, EventContext context);

    @Override
    default void onEvent(T event, EventEndpoint endpoint, EventContext context) {
        onEvent(event, (RabbitEventEndpoint) endpoint, context);
    }

    void onEvent(T event, RabbitEventEndpoint endpoint, EventContext context);
}
