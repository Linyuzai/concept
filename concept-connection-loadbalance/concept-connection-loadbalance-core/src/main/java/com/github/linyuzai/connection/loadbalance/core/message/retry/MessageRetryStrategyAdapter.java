package com.github.linyuzai.connection.loadbalance.core.message.retry;

import com.github.linyuzai.connection.loadbalance.core.scope.Scoped;

/**
 * 消息重试策略适配器。
 * <p>
 * Adapter of retry strategy for message sending.
 */
public interface MessageRetryStrategyAdapter extends Scoped {

    /**
     * 根据连接类型获得消息重试策略。
     * <p>
     * Get retry strategy for message sending by connection's type.
     */
    MessageRetryStrategy getMessageRetryStrategy(String type);
}
