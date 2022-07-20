package com.github.linyuzai.event.kafka;

import com.github.linyuzai.event.core.codec.EventDecoder;
import com.github.linyuzai.event.core.context.EventContext;
import com.github.linyuzai.event.core.error.EventErrorHandler;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.springframework.kafka.listener.AcknowledgingMessageListener;
import org.springframework.kafka.listener.ContainerProperties;
import org.springframework.kafka.listener.MessageListener;

import java.lang.reflect.Type;

public abstract class DefaultKafkaEventSubscriber<T> extends AbstractKafkaEventSubscriber<T> {

    @Override
    public MessageListener<?, ?> createMessageListener(Type type, KafkaEventEndpoint endpoint, EventContext context) {
        ContainerProperties.AckMode mode = endpoint.getProperties().getListener().getAckMode();
        if (mode == ContainerProperties.AckMode.MANUAL || mode == ContainerProperties.AckMode.MANUAL_IMMEDIATE) {
            return (AcknowledgingMessageListener<Object, Object>) (data, acknowledgment) ->
                    handleMessage(data, type, endpoint, context, acknowledgment::acknowledge);
        } else {
            return (MessageListener<Object, Object>) data -> handleMessage(data, type, endpoint, context, null);
        }
    }

    @SuppressWarnings("unchecked")
    private void handleMessage(ConsumerRecord<Object, Object> data,
                               Type type,
                               KafkaEventEndpoint endpoint,
                               EventContext context,
                               Runnable runnable) {
        EventErrorHandler errorHandler = context.get(EventErrorHandler.class);
        try {
            Object value = data.value();
            EventDecoder decoder = context.get(EventDecoder.class);
            onEvent((T) (decoder == null ? value : decoder.decode(value, type)));
            if (runnable != null) {
                runnable.run();
            }
        } catch (Throwable e) {
            errorHandler.onError(e, endpoint, context);
        }
    }
}
