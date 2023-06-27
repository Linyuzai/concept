package com.github.linyuzai.connection.loadbalance.core.message.retry;

import com.github.linyuzai.connection.loadbalance.core.exception.ConnectionLoadBalanceException;

public class MessageRetryException extends ConnectionLoadBalanceException {

    public MessageRetryException(String message) {
        super(message);
    }

    public MessageRetryException(String message, Throwable cause) {
        super(message, cause);
    }
}
