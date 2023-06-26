package com.github.linyuzai.connection.loadbalance.autoconfigure.rabbitmq;

import com.github.linyuzai.connection.loadbalance.core.subscribe.AbstractConnectionSubscriberFactory;
import com.github.linyuzai.connection.loadbalance.core.subscribe.ConnectionSubscriber;
import lombok.Getter;
import lombok.Setter;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.rabbit.listener.MessageListenerContainer;
import org.springframework.amqp.rabbit.listener.RabbitListenerContainerFactory;

@Getter
@Setter
public class RabbitFanoutConnectionSubscriberFactory extends AbstractConnectionSubscriberFactory {

    private RabbitTemplate rabbitTemplate;

    private RabbitListenerContainerFactory<? extends MessageListenerContainer> rabbitListenerContainerFactory;

    @Override
    public ConnectionSubscriber create(String scope) {
        return new RabbitFanoutConnectionSubscriber(rabbitTemplate, rabbitListenerContainerFactory);
    }
}
