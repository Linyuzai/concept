package com.github.linyuzai.event.rabbitmq.publisher;

import com.github.linyuzai.event.core.context.EventContext;
import com.github.linyuzai.event.core.endpoint.EventEndpoint;
import com.github.linyuzai.event.core.publisher.EventPublisher;
import com.github.linyuzai.event.rabbitmq.endpoint.RabbitEventEndpoint;

public interface RabbitEventPublisher extends EventPublisher {

    @Override
    default void publish(Object event, EventEndpoint endpoint, EventContext context) {
        if (endpoint instanceof RabbitEventEndpoint) {
            publish(event, (RabbitEventEndpoint) endpoint, context);
        }
    }

    void publish(Object event, RabbitEventEndpoint endpoint, EventContext context);
}
