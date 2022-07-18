package com.github.linyuzai.event.kafka;

import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.springframework.kafka.listener.AcknowledgingMessageListener;
import org.springframework.kafka.listener.MessageListenerContainer;
import org.springframework.kafka.support.Acknowledgment;

public abstract class AbstractKafkaEventSubscriber implements KafkaEventSubscriber {

    @Override
    public void subscribe(KafkaEventEndpoint endpoint) {
        MessageListenerContainer container = createContainer(endpoint);
        container.getContainerProperties().setMessageListener(new DefaultKafkaEventMessageListener());
        container.start();
    }

    public abstract MessageListenerContainer createContainer(KafkaEventEndpoint endpoint);

    public abstract void onEvent(Object event);

    public class DefaultKafkaEventMessageListener implements AcknowledgingMessageListener<Object, Object> {

        @Override
        public void onMessage(ConsumerRecord<Object, Object> data, Acknowledgment acknowledgment) {
            onEvent(data.value());
            acknowledgment.acknowledge();
        }
    }
}
