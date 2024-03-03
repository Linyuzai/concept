package com.github.linyuzai.connection.loadbalance.autoconfigure.subscribe.rabbitmq;

import com.github.linyuzai.connection.loadbalance.core.concept.Connection;
import com.github.linyuzai.connection.loadbalance.core.concept.ConnectionLoadBalanceConcept;
import com.github.linyuzai.connection.loadbalance.core.server.ConnectionServer;
import com.github.linyuzai.connection.loadbalance.core.subscribe.ConnectionSubscriber;
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

/**
 * RabbitMQ 连接订阅器。
 * 使用 fanout 交换机，通过 {@link RabbitTemplate} 转发消息，通过 {@link RabbitListenerContainerFactory} 订阅消息
 * <p>
 * {@link ConnectionSubscriber} impl by RabbitMQ.
 * Use fanout exchange, forward message by {@link RabbitTemplate}, listen message by {@link RabbitListenerContainerFactory}.
 */
@Getter
@RequiredArgsConstructor
public class RabbitFanoutConnectionSubscriber extends AbstractMasterSlaveConnectionSubscriber {

    private final RabbitTemplate rabbitTemplate;

    private final RabbitListenerContainerFactory<? extends MessageListenerContainer> rabbitListenerContainerFactory;

    /**
     * 创建 RabbitMQ 的监听连接。
     * <p>
     * Create the connection to listen message from RabbitMQ.
     */
    @Override
    protected Connection createSubscriber(String id, String topic, Map<Object, Object> context,
                                          ConnectionLoadBalanceConcept concept) {
        RabbitFanoutSubscriberConnection connection = new RabbitFanoutSubscriberConnection();
        connection.setId(id);

        //需要手动创建 交换机，队列，绑定关系
        RabbitAdmin admin = new RabbitAdmin(rabbitTemplate);
        FanoutExchange exchange = new FanoutExchange(topic);
        Queue queue = new Queue(id);
        Binding binding = BindingBuilder.bind(queue).to(exchange);
        admin.declareExchange(exchange);
        admin.declareQueue(queue);
        admin.declareBinding(binding);
        MessageListenerContainer container = createMessageListenerContainer2(id,
                (ChannelAwareMessageListener) (message, channel) -> {
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

    @Deprecated
    protected MessageListenerContainer createMessageListenerContainer(String queue, MessageListener messageListener) {
        AcknowledgeRabbitListenerEndpoint endpoint =
                new AcknowledgeRabbitListenerEndpoint(AcknowledgeMode.MANUAL);
        MessageListenerContainer container = rabbitListenerContainerFactory.createListenerContainer(endpoint);
        container.setQueueNames(queue);
        container.setupMessageListener(messageListener);
        return container;
    }

    protected MessageListenerContainer createMessageListenerContainer2(String queue, MessageListener messageListener) {
        AcknowledgeRabbitListenerEndpoint2 endpoint =
                new AcknowledgeRabbitListenerEndpoint2(queue, AcknowledgeMode.MANUAL, messageListener);
        return rabbitListenerContainerFactory.createListenerContainer(endpoint);
    }

    /**
     * 创建 RabbitMQ 的转发连接。
     * <p>
     * Create the connection to forward message by RabbitMQ.
     */
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
