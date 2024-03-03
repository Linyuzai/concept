package com.github.linyuzai.connection.loadbalance.autoconfigure.subscribe.rabbitmq;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.amqp.core.AcknowledgeMode;
import org.springframework.amqp.rabbit.listener.MessageListenerContainer;
import org.springframework.amqp.rabbit.listener.RabbitListenerEndpoint;

/**
 * 指定确认模式的监听端点。
 * <p>
 * {@link RabbitListenerEndpoint} specified {@link AcknowledgeMode}.
 */
@Deprecated
@Getter
@RequiredArgsConstructor
public class AcknowledgeRabbitListenerEndpoint implements RabbitListenerEndpoint {

    private final AcknowledgeMode acknowledgeMode;

    @Override
    public AcknowledgeMode getAckMode() {
        return acknowledgeMode;
    }

    @Override
    public String getId() {
        return null;
    }

    @Override
    public String getGroup() {
        return null;
    }

    @Override
    public String getConcurrency() {
        return null;
    }

    @Override
    public Boolean getAutoStartup() {
        return null;
    }

    public Boolean getBatchListener() {
        return null;
    }

    @Override
    public void setupListenerContainer(MessageListenerContainer listenerContainer) {

    }
}
