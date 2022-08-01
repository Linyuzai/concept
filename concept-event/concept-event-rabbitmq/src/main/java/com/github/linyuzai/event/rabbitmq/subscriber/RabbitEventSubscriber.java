package com.github.linyuzai.event.rabbitmq.subscriber;

import com.github.linyuzai.event.core.context.EventContext;
import com.github.linyuzai.event.core.endpoint.EventEndpoint;
import com.github.linyuzai.event.core.subscriber.AbstractEventSubscriber;
import com.github.linyuzai.event.rabbitmq.endpoint.RabbitEventEndpoint;

import java.util.function.Consumer;

public abstract class RabbitEventSubscriber extends AbstractEventSubscriber {

    @Override
    public void doSubscribe(EventEndpoint endpoint, EventContext context, Consumer<Object> consumer) {
        if (endpoint instanceof RabbitEventEndpoint) {
            subscribeRabbit((RabbitEventEndpoint) endpoint, context, consumer);
        }
    }

    public abstract void subscribeRabbit(RabbitEventEndpoint endpoint, EventContext context, Consumer<Object> consumer);
}
