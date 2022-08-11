package com.github.linyuzai.event.kafka.subscriber;

import com.github.linyuzai.event.core.context.EventContext;
import com.github.linyuzai.event.core.error.EventErrorHandler;
import com.github.linyuzai.event.core.listener.EventListener;
import com.github.linyuzai.event.core.subscriber.Subscription;
import com.github.linyuzai.event.kafka.endpoint.KafkaEventEndpoint;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.springframework.kafka.listener.AcknowledgingMessageListener;
import org.springframework.kafka.listener.ContainerProperties;
import org.springframework.kafka.listener.MessageListener;
import org.springframework.kafka.listener.MessageListenerContainer;

public abstract class AbstractKafkaEventSubscriber extends KafkaEventSubscriber {

    @Override
    public Subscription subscribeKafka(EventListener listener, KafkaEventEndpoint endpoint, EventContext context) {
        MessageListenerContainer container = createMessageListenerContainer(endpoint, context);
        container.getContainerProperties().setMessageListener(createMessageListener(listener, endpoint, context));
        container.start();
        return new KafkaSubscription(container);
    }

    public abstract MessageListenerContainer createMessageListenerContainer(KafkaEventEndpoint endpoint, EventContext context);

    public MessageListener<?, ?> createMessageListener(EventListener listener, KafkaEventEndpoint endpoint, EventContext context) {
        ContainerProperties.AckMode mode = endpoint.getProperties().getListener().getAckMode();
        if (mode == ContainerProperties.AckMode.MANUAL || mode == ContainerProperties.AckMode.MANUAL_IMMEDIATE) {
            return (AcknowledgingMessageListener<Object, Object>) (data, acknowledgment) ->
                    handleMessage(data, listener, endpoint, context, acknowledgment::acknowledge);
        } else {
            return (MessageListener<Object, Object>) data ->
                    handleMessage(data, listener, endpoint, context, null);
        }
    }

    public void handleMessage(ConsumerRecord<Object, Object> data,
                              EventListener listener,
                              KafkaEventEndpoint endpoint,
                              EventContext context,
                              Runnable runnable) {
        EventErrorHandler errorHandler = context.get(EventErrorHandler.class);
        try {
            listener.onEvent(getPayload(data, endpoint, context), endpoint, context);
            if (runnable != null) {
                runnable.run();
            }
        } catch (Throwable e) {
            errorHandler.onError(e, endpoint, context);
        }
    }

    public Object getPayload(ConsumerRecord<?, ?> record, KafkaEventEndpoint endpoint, EventContext context) {
        return record.value();
    }
}
