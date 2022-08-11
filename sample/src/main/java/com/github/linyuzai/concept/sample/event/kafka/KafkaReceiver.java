package com.github.linyuzai.concept.sample.event.kafka;

import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.springframework.kafka.config.ConcurrentKafkaListenerContainerFactory;
import org.springframework.kafka.listener.MessageListener;
import org.springframework.kafka.listener.MessageListenerContainer;

public class KafkaReceiver {

    void receive() {
        ConcurrentKafkaListenerContainerFactory<Object, Object> factory =
                new ConcurrentKafkaListenerContainerFactory<>();
        MessageListenerContainer container = factory.createContainer("topic");
        container.getContainerProperties().setMessageListener(new MessageListener<Object, Object>() {
            @Override
            public void onMessage(ConsumerRecord<Object, Object> data) {

            }
        });
        container.start();
    }
}
