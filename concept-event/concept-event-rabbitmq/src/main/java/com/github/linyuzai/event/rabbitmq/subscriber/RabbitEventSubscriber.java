package com.github.linyuzai.event.rabbitmq.subscriber;

import com.github.linyuzai.event.core.context.EventContext;
import com.github.linyuzai.event.core.endpoint.EventEndpoint;
import com.github.linyuzai.event.core.listener.EventListener;
import com.github.linyuzai.event.core.subscriber.AbstractEventSubscriber;
import com.github.linyuzai.event.core.subscriber.Subscription;
import com.github.linyuzai.event.rabbitmq.binding.RabbitBinding;
import com.github.linyuzai.event.rabbitmq.endpoint.RabbitEventEndpoint;

public abstract class RabbitEventSubscriber extends AbstractEventSubscriber {

    @Override
    public Subscription doSubscribe(EventListener listener, EventEndpoint endpoint, EventContext context) {
        RabbitEventEndpoint ree = (RabbitEventEndpoint) endpoint;
        binding(new RabbitBinding(ree.getAdmin()));
        return subscribeRabbit(listener, ree, context);
    }

    public void binding(RabbitBinding binding) {

    }

    @Override
    public boolean support(EventEndpoint endpoint, EventContext context) {
        return endpoint instanceof RabbitEventEndpoint;
    }

    public abstract Subscription subscribeRabbit(EventListener listener,
                                                 RabbitEventEndpoint endpoint,
                                                 EventContext context);
}
