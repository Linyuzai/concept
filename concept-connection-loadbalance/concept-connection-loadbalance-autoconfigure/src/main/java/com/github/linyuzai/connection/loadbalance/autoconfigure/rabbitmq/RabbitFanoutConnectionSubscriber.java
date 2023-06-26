package com.github.linyuzai.connection.loadbalance.autoconfigure.rabbitmq;

import com.github.linyuzai.connection.loadbalance.core.concept.Connection;
import com.github.linyuzai.connection.loadbalance.core.concept.ConnectionLoadBalanceConcept;
import com.github.linyuzai.connection.loadbalance.core.subscribe.AbstractConnectionSubscriber;
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
public class RabbitFanoutConnectionSubscriber extends AbstractConnectionSubscriber {

    private final RabbitTemplate rabbitTemplate;

    private final RabbitListenerContainerFactory<? extends MessageListenerContainer> rabbitListenerContainerFactory;

    @Override
    protected Connection create(String topic, ConnectionLoadBalanceConcept concept) {
        RabbitFanoutConnection connection = new RabbitFanoutConnection(Connection.Type.OBSERVABLE);
        connection.setId(topic);
        connection.setExchange(topic);
        connection.setRabbitTemplate(rabbitTemplate);
        RabbitAdmin admin = new RabbitAdmin(rabbitTemplate);
        admin.declareBinding(BindingBuilder.bind(new Queue(topic + "." + getFrom(concept)))
                .to(new FanoutExchange(topic)));
        MessageListenerContainer listenerContainer = rabbitListenerContainerFactory.createListenerContainer();
        listenerContainer.setQueueNames(topic);
        listenerContainer.setupMessageListener((ChannelAwareMessageListener) (message, channel) -> {
            RabbitFanoutConnectionSubscriber.super.onMessage(connection, message);
            if (channel != null) {
                channel.basicAck(message.getMessageProperties().getDeliveryTag(), false);
            }
        });
        listenerContainer.afterPropertiesSet();
        listenerContainer.start();
        return connection;
    }

    @Override
    protected String getExtension() {
        return "Rabbit";
    }
}
