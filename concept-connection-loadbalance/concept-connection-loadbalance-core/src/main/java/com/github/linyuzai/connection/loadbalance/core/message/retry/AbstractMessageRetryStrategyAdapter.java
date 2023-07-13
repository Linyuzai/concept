package com.github.linyuzai.connection.loadbalance.core.message.retry;

import com.github.linyuzai.connection.loadbalance.core.concept.Connection;
import com.github.linyuzai.connection.loadbalance.core.scope.AbstractScoped;
import lombok.Getter;
import lombok.Setter;

/**
 * 消息重试策略适配器的抽象类。
 * <p>
 * Abstract adapter of retry strategy for message sending.
 */
@Getter
@Setter
public abstract class AbstractMessageRetryStrategyAdapter extends AbstractScoped
        implements MessageRetryStrategyAdapter {

    @Override
    public MessageRetryStrategy getMessageRetryStrategy(String type) {
        switch (type) {
            case Connection.Type.CLIENT:
                return getClientMessageRetryStrategy();
            case Connection.Type.SUBSCRIBER:
                return getSubscribeMessageRetryStrategy();
            case Connection.Type.OBSERVABLE:
                return getForwardMessageRetryStrategy();
            default:
                return getUndefinedTypeMessageEncoder(type);
        }
    }

    /**
     * 获得客户端连接的消息重试策略。
     * <p>
     * Get retry strategy for message sending of client connection.
     */
    public abstract MessageRetryStrategy getClientMessageRetryStrategy();

    /**
     * 获得订阅连接的消息重试策略。
     * <p>
     * Get retry strategy for message sending of subscription.
     */
    public abstract MessageRetryStrategy getSubscribeMessageRetryStrategy();

    /**
     * 获得转发连接的消息重试策略。
     * <p>
     * Get retry strategy for message sending of forward.
     */
    public abstract MessageRetryStrategy getForwardMessageRetryStrategy();

    public MessageRetryStrategy getUndefinedTypeMessageEncoder(String type) {
        throw new UnsupportedOperationException();
    }
}
