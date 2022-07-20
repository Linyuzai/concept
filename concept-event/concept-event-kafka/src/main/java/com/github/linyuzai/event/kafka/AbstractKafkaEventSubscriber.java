package com.github.linyuzai.event.kafka;

import com.github.linyuzai.event.core.context.EventContext;
import org.springframework.kafka.listener.MessageListener;
import org.springframework.kafka.listener.MessageListenerContainer;

import java.lang.reflect.Type;

public abstract class AbstractKafkaEventSubscriber<T> implements KafkaEventSubscriber<T> {

    @Override
    public void subscribe(Type type, KafkaEventEndpoint endpoint, EventContext context) {
        MessageListenerContainer container = createContainer(type, endpoint, context);
        container.getContainerProperties().setMessageListener(createMessageListener(type, endpoint, context));
        container.start();
    }

    public abstract MessageListenerContainer createContainer(Type type, KafkaEventEndpoint endpoint, EventContext context);

    public abstract MessageListener<?, ?> createMessageListener(Type type, KafkaEventEndpoint endpoint, EventContext context);
}
