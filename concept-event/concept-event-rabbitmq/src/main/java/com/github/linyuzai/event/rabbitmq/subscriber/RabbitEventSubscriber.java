package com.github.linyuzai.event.rabbitmq.subscriber;

import com.github.linyuzai.event.core.context.EventContext;
import com.github.linyuzai.event.core.endpoint.EventEndpoint;
import com.github.linyuzai.event.core.subscriber.AbstractEventSubscriber;
import com.github.linyuzai.event.core.subscriber.Subscription;
import com.github.linyuzai.event.rabbitmq.endpoint.RabbitEventEndpoint;

import java.util.function.Consumer;

public abstract class RabbitEventSubscriber extends AbstractEventSubscriber {

    @Override
    public Subscription doSubscribe(EventEndpoint endpoint, EventContext context, Consumer<Object> consumer) {
        return subscribeRabbit((RabbitEventEndpoint) endpoint, context, consumer);
    }

    @Override
    public boolean support(EventEndpoint endpoint, EventContext context) {
        return endpoint instanceof RabbitEventEndpoint;
    }

    public abstract Subscription subscribeRabbit(RabbitEventEndpoint endpoint, EventContext context, Consumer<Object> consumer);
}
