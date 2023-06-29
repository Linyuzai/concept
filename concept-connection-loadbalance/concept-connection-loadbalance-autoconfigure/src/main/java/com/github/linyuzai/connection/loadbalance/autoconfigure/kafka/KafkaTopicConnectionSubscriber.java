package com.github.linyuzai.connection.loadbalance.autoconfigure.kafka;

import com.github.linyuzai.connection.loadbalance.core.concept.Connection;
import com.github.linyuzai.connection.loadbalance.core.concept.ConnectionLoadBalanceConcept;
import com.github.linyuzai.connection.loadbalance.core.subscribe.AbstractMasterSlaveConnectionSubscriber;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.springframework.kafka.config.KafkaListenerContainerFactory;
import org.springframework.kafka.config.KafkaListenerEndpoint;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.listener.AcknowledgingMessageListener;
import org.springframework.kafka.listener.ContainerProperties;
import org.springframework.kafka.listener.MessageListener;
import org.springframework.kafka.listener.MessageListenerContainer;

@Getter
@RequiredArgsConstructor
public class KafkaTopicConnectionSubscriber extends AbstractMasterSlaveConnectionSubscriber {

    private final KafkaTemplate<?, Object> kafkaTemplate;

    private final KafkaListenerContainerFactory<? extends MessageListenerContainer> kafkaListenerContainerFactory;

    @Override
    protected Connection create(String topic, String name, ConnectionLoadBalanceConcept concept) {
        KafkaTopicConnection connection = new KafkaTopicConnection(Connection.Type.OBSERVABLE);
        connection.setId(topic);
        connection.setTopic(topic);
        connection.setKafkaTemplate(kafkaTemplate);
        KafkaListenerEndpoint endpoint = new GroupTopicKafkaListenerEndpoint(name, topic);
        MessageListenerContainer container = kafkaListenerContainerFactory.createListenerContainer(endpoint);
        ContainerProperties.AckMode mode = container.getContainerProperties().getAckMode();
        Object listener;
        if (mode == ContainerProperties.AckMode.MANUAL || mode == ContainerProperties.AckMode.MANUAL_IMMEDIATE) {
            listener = (AcknowledgingMessageListener<Object, Object>) (data, acknowledgment) -> {
                onMessage(connection, getPayload(data));
                if (acknowledgment != null) {
                    acknowledgment.acknowledge();
                }
            };
        } else {
            listener = (MessageListener<Object, Object>) data ->
                    concept.onMessage(connection, getPayload(data));
        }
        connection.setCloseCallback(reason -> {
            if (container.isRunning()) {
                container.stop();
            }
        });
        container.setupMessageListener(listener);
        container.start();
        return connection;
    }

    protected Object getPayload(ConsumerRecord<Object, Object> record) {
        return record.value();
    }

    @Override
    protected String getExtension() {
        return "Kafka";
    }
}
