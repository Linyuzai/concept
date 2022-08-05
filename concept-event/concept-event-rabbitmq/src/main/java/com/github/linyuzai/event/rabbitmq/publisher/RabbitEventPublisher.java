package com.github.linyuzai.event.rabbitmq.publisher;

import com.github.linyuzai.event.core.context.EventContext;
import com.github.linyuzai.event.core.endpoint.EventEndpoint;
import com.github.linyuzai.event.core.publisher.AbstractEventPublisher;
import com.github.linyuzai.event.rabbitmq.endpoint.RabbitEventEndpoint;

public abstract class RabbitEventPublisher extends AbstractEventPublisher {

    @Override
    public void doPublish(Object event, EventEndpoint endpoint, EventContext context) {
        if (endpoint instanceof RabbitEventEndpoint) {
            publishRabbit(event, (RabbitEventEndpoint) endpoint, context);
        }
    }

    public abstract void publishRabbit(Object event, RabbitEventEndpoint endpoint, EventContext context);
}
