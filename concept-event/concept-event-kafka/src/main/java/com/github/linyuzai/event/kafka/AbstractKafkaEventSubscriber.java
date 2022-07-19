package com.github.linyuzai.event.kafka;

import com.github.linyuzai.event.core.codec.EventDecoder;
import com.github.linyuzai.event.core.context.EventContext;
import lombok.AllArgsConstructor;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.springframework.kafka.listener.AcknowledgingMessageListener;
import org.springframework.kafka.listener.MessageListenerContainer;
import org.springframework.kafka.support.Acknowledgment;

public abstract class AbstractKafkaEventSubscriber implements KafkaEventSubscriber {

    @Override
    public void subscribe(KafkaEventEndpoint endpoint, EventContext context) {
        MessageListenerContainer container = createContainer(endpoint);
        container.getContainerProperties().setMessageListener(new DefaultKafkaEventMessageListener(context));
        container.start();
    }

    public abstract MessageListenerContainer createContainer(KafkaEventEndpoint endpoint);

    public abstract void onEvent(Object event);

    @AllArgsConstructor
    public class DefaultKafkaEventMessageListener implements AcknowledgingMessageListener<Object, Object> {

        private EventContext context;

        @Override
        public void onMessage(ConsumerRecord<Object, Object> data, Acknowledgment acknowledgment) {
            Object value = data.value();
            EventDecoder decoder = context.get(EventDecoder.class);
            onEvent(decoder == null ? value : decoder.decode(value));
            acknowledgment.acknowledge();
        }
    }
}
