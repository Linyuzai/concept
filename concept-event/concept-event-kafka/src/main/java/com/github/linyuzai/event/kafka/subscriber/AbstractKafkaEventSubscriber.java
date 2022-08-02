package com.github.linyuzai.event.kafka.subscriber;

import com.github.linyuzai.event.core.context.EventContext;
import com.github.linyuzai.event.core.subscriber.Subscription;
import com.github.linyuzai.event.kafka.endpoint.KafkaEventEndpoint;
import org.springframework.kafka.listener.MessageListener;
import org.springframework.kafka.listener.MessageListenerContainer;

import java.util.function.Consumer;

public abstract class AbstractKafkaEventSubscriber extends KafkaEventSubscriber {

    @Override
    public Subscription subscribeKafka(KafkaEventEndpoint endpoint, EventContext context, Consumer<Object> consumer) {
        MessageListenerContainer container = createContainer(endpoint, context);
        container.getContainerProperties().setMessageListener(createMessageListener(endpoint, context, consumer));
        container.start();
        return new KafkaSubscription(container);
    }

    public abstract MessageListenerContainer createContainer(KafkaEventEndpoint endpoint, EventContext context);

    public abstract MessageListener<?, ?> createMessageListener(KafkaEventEndpoint endpoint, EventContext context, Consumer<Object> consumer);
}
