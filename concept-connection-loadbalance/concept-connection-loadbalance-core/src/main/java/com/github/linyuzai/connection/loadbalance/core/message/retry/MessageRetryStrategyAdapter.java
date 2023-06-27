package com.github.linyuzai.connection.loadbalance.core.message.retry;

import com.github.linyuzai.connection.loadbalance.core.scope.Scoped;

public interface MessageRetryStrategyAdapter extends Scoped {

    MessageRetryStrategy getMessageRetryStrategy(String type);
}
