package com.github.linyuzai.connection.loadbalance.autoconfigure.subscribe.rabbitmq;

import com.github.linyuzai.connection.loadbalance.core.concept.Connection;
import com.github.linyuzai.connection.loadbalance.core.concept.ConnectionLoadBalanceConcept;
import com.github.linyuzai.connection.loadbalance.core.server.ConnectionServer;
import com.github.linyuzai.connection.loadbalance.core.subscribe.masterslave.AbstractMasterSlaveConnectionSubscriber;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.amqp.core.*;
import org.springframework.amqp.rabbit.core.RabbitAdmin;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.rabbit.listener.MessageListenerContainer;
import org.springframework.amqp.rabbit.listener.RabbitListenerContainerFactory;
import org.springframework.amqp.rabbit.listener.api.ChannelAwareMessageListener;

import java.net.URI;
import java.util.Map;

@Getter
@RequiredArgsConstructor
public class RabbitFanoutConnectionSubscriber extends AbstractMasterSlaveConnectionSubscriber {

    private final RabbitTemplate rabbitTemplate;

    private final RabbitListenerContainerFactory<? extends MessageListenerContainer> rabbitListenerContainerFactory;

    @Override
    protected Connection createSubscriber(String id, String topic, Map<Object, Object> context,
                                          ConnectionLoadBalanceConcept concept) {
        RabbitFanoutSubscriberConnection connection = new RabbitFanoutSubscriberConnection();
        connection.setId(id);
        RabbitAdmin admin = new RabbitAdmin(rabbitTemplate);
        admin.declareBinding(BindingBuilder.bind(new Queue(id))
                .to(new FanoutExchange(topic)));
        MessageListenerContainer container = createMessageListenerContainer();
        container.setQueueNames(topic);
        container.setupMessageListener((ChannelAwareMessageListener) (message, channel) -> {
            onMessageReceived(connection, message);
            if (channel != null) {
                channel.basicAck(message.getMessageProperties().getDeliveryTag(), false);
            }
        });
        container.afterPropertiesSet();
        container.start();
        connection.setContainer(container);
        return connection;
    }

    protected MessageListenerContainer createMessageListenerContainer() {
        return rabbitListenerContainerFactory.createListenerContainer();
    }

    @Override
    protected Connection createObservable(String id, String topic, Map<Object, Object> context,
                                          ConnectionLoadBalanceConcept concept) {
        RabbitFanoutObservableConnection connection = new RabbitFanoutObservableConnection();
        connection.setId(id);
        connection.setExchange(topic);
        connection.setRabbitTemplate(rabbitTemplate);
        return connection;
    }

    @Override
    protected ConnectionServer getSubscribeServer() {
        return new RabbitConnectionServer(rabbitTemplate);
    }

    @Getter
    @RequiredArgsConstructor
    public static class RabbitConnectionServer implements ConnectionServer {

        private final RabbitTemplate rabbitTemplate;

        @Override
        public String getInstanceId() {
            return null;
        }

        @Override
        public String getServiceId() {
            return "rabbit";
        }

        @Override
        public String getHost() {
            return rabbitTemplate.getConnectionFactory().getHost();
        }

        @Override
        public int getPort() {
            return rabbitTemplate.getConnectionFactory().getPort();
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
