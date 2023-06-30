package com.github.linyuzai.connection.loadbalance.autoconfigure.rabbitmq;

import com.github.linyuzai.connection.loadbalance.core.concept.Connection;
import com.github.linyuzai.connection.loadbalance.core.concept.ConnectionLoadBalanceConcept;
import com.github.linyuzai.connection.loadbalance.core.subscribe.AbstractMasterSlaveConnectionSubscriber;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.amqp.core.*;
import org.springframework.amqp.rabbit.core.RabbitAdmin;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.rabbit.listener.MessageListenerContainer;
import org.springframework.amqp.rabbit.listener.RabbitListenerContainerFactory;
import org.springframework.amqp.rabbit.listener.api.ChannelAwareMessageListener;

@Getter
@RequiredArgsConstructor
public class RabbitFanoutConnectionSubscriber extends AbstractMasterSlaveConnectionSubscriber {

    private final RabbitTemplate rabbitTemplate;

    private final RabbitListenerContainerFactory<? extends MessageListenerContainer> rabbitListenerContainerFactory;

    @Override
    protected Connection create(String topic, String name, ConnectionLoadBalanceConcept concept) {
        RabbitFanoutConnection connection = new RabbitFanoutConnection(Connection.Type.OBSERVABLE);
        connection.setId(topic);
        connection.setExchange(topic);
        connection.setRabbitTemplate(rabbitTemplate);
        RabbitAdmin admin = new RabbitAdmin(rabbitTemplate);
        admin.declareBinding(BindingBuilder.bind(new Queue(name))
                .to(new FanoutExchange(topic)));
        MessageListenerContainer container = rabbitListenerContainerFactory.createListenerContainer();
        container.setQueueNames(topic);
        container.setupMessageListener((ChannelAwareMessageListener) (message, channel) -> {
            onMessageReceived(connection, message);
            if (channel != null) {
                channel.basicAck(message.getMessageProperties().getDeliveryTag(), false);
            }
        });
        connection.setCloseCallback(reason -> {
            if (container.isRunning()) {
                container.stop();
            }
        });
        container.afterPropertiesSet();
        container.start();
        return connection;
    }

    @Override
    protected String getExtension() {
        return "Rabbit";
    }
}
