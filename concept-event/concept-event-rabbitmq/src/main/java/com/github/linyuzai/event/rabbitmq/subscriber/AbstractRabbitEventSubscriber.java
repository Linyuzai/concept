package com.github.linyuzai.event.rabbitmq.subscriber;

import com.github.linyuzai.event.core.context.EventContext;
import com.github.linyuzai.event.core.subscriber.Subscription;
import com.github.linyuzai.event.rabbitmq.endpoint.RabbitEventEndpoint;
import org.springframework.amqp.core.MessageListener;
import org.springframework.amqp.rabbit.listener.MessageListenerContainer;

import java.util.function.Consumer;

public abstract class AbstractRabbitEventSubscriber extends RabbitEventSubscriber {

    @Override
    public Subscription subscribeRabbit(RabbitEventEndpoint endpoint, EventContext context, Consumer<Object> consumer) {
        MessageListenerContainer container = createMessageListenerContainer(endpoint, context);
        container.setupMessageListener(createMessageListener(endpoint, context, consumer));
        container.start();
        return new RabbitSubscription(container);
    }

    public abstract MessageListenerContainer createMessageListenerContainer(RabbitEventEndpoint endpoint, EventContext context);

    public abstract MessageListener createMessageListener(RabbitEventEndpoint endpoint, EventContext context, Consumer<Object> consumer);
}
