package com.github.linyuzai.connection.loadbalance.core.message.retry;

import com.github.linyuzai.connection.loadbalance.core.exception.ConnectionLoadBalanceException;
import lombok.Getter;

@Getter
public class MessageRetryException extends ConnectionLoadBalanceException {

    private final Throwable retryFor;

    public MessageRetryException(String message, Throwable retryFor) {
        super(message);
        this.retryFor = retryFor;
    }

    public MessageRetryException(String message, Throwable cause, Throwable retryFor) {
        super(message, cause);
        this.retryFor = retryFor;
    }
}
