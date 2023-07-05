package com.github.linyuzai.connection.loadbalance.autoconfigure.kafka;

import com.github.linyuzai.connection.loadbalance.core.concept.Connection;
import com.github.linyuzai.connection.loadbalance.core.concept.ConnectionLoadBalanceConcept;
import com.github.linyuzai.connection.loadbalance.core.server.ConnectionServer;
import com.github.linyuzai.connection.loadbalance.core.subscribe.masterslave.AbstractMasterSlaveConnectionSubscriber;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.kafka.config.KafkaListenerContainerFactory;
import org.springframework.kafka.config.KafkaListenerEndpoint;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.listener.AcknowledgingMessageListener;
import org.springframework.kafka.listener.ContainerProperties;
import org.springframework.kafka.listener.MessageListener;
import org.springframework.kafka.listener.MessageListenerContainer;

import java.net.URI;
import java.util.Map;

@Getter
@RequiredArgsConstructor
public class KafkaTopicConnectionSubscriber extends AbstractMasterSlaveConnectionSubscriber {

    private final KafkaTemplate<?, Object> kafkaTemplate;

    private final KafkaListenerContainerFactory<? extends MessageListenerContainer> kafkaListenerContainerFactory;

    @Override
    protected Connection create(String topic, String name, Connection subscriber, ConnectionLoadBalanceConcept concept) {
        KafkaTopicConnection connection = new KafkaTopicConnection(Connection.Type.OBSERVABLE);
        connection.setId(name);
        connection.setTopic(topic);
        connection.setKafkaTemplate(kafkaTemplate);
        KafkaListenerEndpoint endpoint = new GroupTopicKafkaListenerEndpoint(name, topic);
        MessageListenerContainer container = kafkaListenerContainerFactory.createListenerContainer(endpoint);
        ContainerProperties.AckMode mode = container.getContainerProperties().getAckMode();
        Object listener;
        if (mode == ContainerProperties.AckMode.MANUAL || mode == ContainerProperties.AckMode.MANUAL_IMMEDIATE) {
            listener = (AcknowledgingMessageListener<Object, Object>) (data, acknowledgment) -> {
                onMessageReceived(subscriber, data);
                if (acknowledgment != null) {
                    acknowledgment.acknowledge();
                }
            };
        } else {
            listener = (MessageListener<Object, Object>) data ->
                    onMessageReceived(subscriber, data);
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

    @Override
    protected ConnectionServer getSubscriberServer() {
        return new KafkaConnectionServer(kafkaTemplate);
    }

    @Getter
    @RequiredArgsConstructor
    public static class KafkaConnectionServer implements ConnectionServer {

        private final KafkaTemplate<?, Object> kafkaTemplate;

        @Override
        public String getInstanceId() {
            return null;
        }

        @Override
        public String getServiceId() {
            return "kafka";
        }

        @Override
        public String getHost() {
            return null;
        }

        @Override
        public int getPort() {
            return 0;
        }

        @Override
        public Map<String, String> getMetadata() {
            return null;
        }

        @Override
        public URI getUri() {
            return null;
        }

        @Override
        public String getScheme() {
            return null;
        }

        @Override
        public boolean isSecure() {
            return false;
        }
    }
}
