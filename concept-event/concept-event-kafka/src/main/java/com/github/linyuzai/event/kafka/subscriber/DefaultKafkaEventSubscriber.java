package com.github.linyuzai.event.kafka.subscriber;

import com.github.linyuzai.event.core.context.EventContext;
import com.github.linyuzai.event.core.error.EventErrorHandler;
import com.github.linyuzai.event.kafka.endpoint.KafkaEventEndpoint;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.springframework.kafka.listener.AcknowledgingMessageListener;
import org.springframework.kafka.listener.ContainerProperties;
import org.springframework.kafka.listener.MessageListener;

import java.util.function.Consumer;

public abstract class DefaultKafkaEventSubscriber extends AbstractKafkaEventSubscriber {

    @Override
    public MessageListener<?, ?> createMessageListener(KafkaEventEndpoint endpoint, EventContext context, Consumer<Object> consumer) {
        ContainerProperties.AckMode mode = endpoint.getProperties().getListener().getAckMode();
        if (mode == ContainerProperties.AckMode.MANUAL || mode == ContainerProperties.AckMode.MANUAL_IMMEDIATE) {
            return (AcknowledgingMessageListener<Object, Object>) (data, acknowledgment) ->
                    handleMessage(data, endpoint, context, acknowledgment::acknowledge, consumer);
        } else {
            return (MessageListener<Object, Object>) data ->
                    handleMessage(data, endpoint, context, null, consumer);
        }
    }

    public void handleMessage(ConsumerRecord<Object, Object> data,
                              KafkaEventEndpoint endpoint,
                              EventContext context,
                              Runnable runnable,
                              Consumer<Object> consumer) {
        EventErrorHandler errorHandler = context.get(EventErrorHandler.class);
        try {
            consumer.accept(getPayload(data, endpoint, context));
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
