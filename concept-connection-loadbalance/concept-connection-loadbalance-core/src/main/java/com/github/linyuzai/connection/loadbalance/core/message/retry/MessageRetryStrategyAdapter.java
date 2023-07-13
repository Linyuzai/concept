package com.github.linyuzai.connection.loadbalance.core.message.retry;

import com.github.linyuzai.connection.loadbalance.core.scope.Scoped;

/**
 * 消息重试策略适配器。
 * <p>
 * Adapter of retry strategy for message sending.
 */
public interface MessageRetryStrategyAdapter extends Scoped {

    MessageRetryStrategy getMessageRetryStrategy(String type);
}
