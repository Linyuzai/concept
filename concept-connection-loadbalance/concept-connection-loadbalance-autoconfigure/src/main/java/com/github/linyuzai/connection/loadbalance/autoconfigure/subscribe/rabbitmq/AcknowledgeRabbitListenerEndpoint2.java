package com.github.linyuzai.connection.loadbalance.autoconfigure.subscribe.rabbitmq;

import lombok.Getter;
import org.springframework.amqp.core.AcknowledgeMode;
import org.springframework.amqp.core.MessageListener;
import org.springframework.amqp.rabbit.listener.AbstractRabbitListenerEndpoint;
import org.springframework.amqp.rabbit.listener.MessageListenerContainer;
import org.springframework.amqp.rabbit.listener.RabbitListenerEndpoint;

/**
 * 指定确认模式的监听端点。
 * <p>
 * {@link RabbitListenerEndpoint} specified {@link AcknowledgeMode}.
 */
@Getter
public class AcknowledgeRabbitListenerEndpoint2 extends AbstractRabbitListenerEndpoint {

    private final MessageListener messageListener;

    public AcknowledgeRabbitListenerEndpoint2(String queue, AcknowledgeMode acknowledgeMode, MessageListener messageListener) {
        setQueueNames(queue);
        setAckMode(acknowledgeMode);
        this.messageListener = messageListener;
    }

    @Override
    protected MessageListener createMessageListener(MessageListenerContainer container) {
        return messageListener;
    }
}
