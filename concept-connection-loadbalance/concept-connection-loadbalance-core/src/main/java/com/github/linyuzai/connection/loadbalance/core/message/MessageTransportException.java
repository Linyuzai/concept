package com.github.linyuzai.connection.loadbalance.core.message;

import com.github.linyuzai.connection.loadbalance.core.exception.ConnectionLoadBalanceException;

/**
 * 用于触发重试或切换主从订阅器。
 * <p>
 * Used to trigger a retry or switch between master and slave subscribers.
 */
public class MessageTransportException extends ConnectionLoadBalanceException {

    public MessageTransportException(String message) {
        super(message);
    }

    public MessageTransportException(Throwable cause) {
        super(cause.getMessage(), cause);
    }

    public MessageTransportException(String message, Throwable cause) {
        super(message, cause);
    }
}
