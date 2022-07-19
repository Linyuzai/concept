package com.github.linyuzai.event.kafka;

import com.github.linyuzai.event.core.context.EventContext;
import com.github.linyuzai.event.core.endpoint.EventEndpoint;
import com.github.linyuzai.event.core.publisher.EventPublisher;

public interface KafkaEventPublisher extends EventPublisher {

    @Override
    default void publish(Object event, EventEndpoint endpoint, EventContext context) {
        if (endpoint instanceof KafkaEventEndpoint) {
            publish(event, (KafkaEventEndpoint) endpoint, context);
        }
    }

    void publish(Object event, KafkaEventEndpoint endpoint, EventContext context);
}
