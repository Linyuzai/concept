package com.github.linyuzai.event.kafka;

import com.github.linyuzai.event.core.context.EventContext;
import com.github.linyuzai.event.core.endpoint.EventEndpoint;
import com.github.linyuzai.event.core.subscriber.GenericEventSubscriber;

public interface KafkaEventSubscriber<T> extends GenericEventSubscriber<T> {

    @Override
    default void subscribe(EventEndpoint endpoint, EventContext context) {
        if (endpoint instanceof KafkaEventEndpoint) {
            subscribe((KafkaEventEndpoint) endpoint, context);
        }
    }

    void subscribe(KafkaEventEndpoint endpoint, EventContext context);
}
