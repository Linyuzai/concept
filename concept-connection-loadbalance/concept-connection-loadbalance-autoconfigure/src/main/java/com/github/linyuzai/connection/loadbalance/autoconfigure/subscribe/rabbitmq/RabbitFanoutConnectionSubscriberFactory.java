package com.github.linyuzai.connection.loadbalance.autoconfigure.subscribe.rabbitmq;

import com.github.linyuzai.connection.loadbalance.core.subscribe.masterslave.MasterSlaveConnectionSubscriber;
import com.github.linyuzai.connection.loadbalance.core.subscribe.masterslave.MasterSlaveConnectionSubscriberFactory;
import lombok.Getter;
import lombok.Setter;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.rabbit.listener.MessageListenerContainer;
import org.springframework.amqp.rabbit.listener.RabbitListenerContainerFactory;

/**
 * RabbitMQ 连接订阅器工厂。
 * <p>
 * Factory of {@link RabbitFanoutConnectionSubscriber}.
 */
@Getter
@Setter
public class RabbitFanoutConnectionSubscriberFactory extends MasterSlaveConnectionSubscriberFactory {

    private RabbitTemplate rabbitTemplate;

    private RabbitListenerContainerFactory<? extends MessageListenerContainer> rabbitListenerContainerFactory;

    @Override
    public MasterSlaveConnectionSubscriber doCreate(String scope) {
        return new RabbitFanoutConnectionSubscriber(rabbitTemplate, rabbitListenerContainerFactory);
    }
}
