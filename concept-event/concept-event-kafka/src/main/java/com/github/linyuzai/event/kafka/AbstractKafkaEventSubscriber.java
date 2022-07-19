package com.github.linyuzai.event.kafka;

import com.github.linyuzai.event.core.codec.EventDecoder;
import com.github.linyuzai.event.core.context.EventContext;
import com.github.linyuzai.event.core.error.EventErrorHandler;
import lombok.AllArgsConstructor;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.springframework.kafka.listener.AcknowledgingMessageListener;
import org.springframework.kafka.listener.MessageListenerContainer;
import org.springframework.kafka.support.Acknowledgment;

public abstract class AbstractKafkaEventSubscriber<T> implements KafkaEventSubscriber<T> {

    @Override
    public void subscribe(KafkaEventEndpoint endpoint, EventContext context) {
        MessageListenerContainer container = createContainer(endpoint);
        container.getContainerProperties()
                .setMessageListener(new DefaultKafkaEventMessageListener(endpoint, context));
        container.start();
    }

    public abstract MessageListenerContainer createContainer(KafkaEventEndpoint endpoint);

    @AllArgsConstructor
    public class DefaultKafkaEventMessageListener implements AcknowledgingMessageListener<Object, Object> {

        private KafkaEventEndpoint endpoint;
        private EventContext context;

        @SuppressWarnings("unchecked")
        @Override
        public void onMessage(ConsumerRecord<Object, Object> data, Acknowledgment acknowledgment) {
            EventErrorHandler errorHandler = context.get(EventErrorHandler.class);
            try {
                Object value = data.value();
                EventDecoder decoder = context.get(EventDecoder.class);
                onEvent((T) (decoder == null ? value : decoder.decode(value)));
                acknowledgment.acknowledge();
            } catch (Throwable e) {
                errorHandler.onError(e, endpoint, context);
            }
        }
    }
}
