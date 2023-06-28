package com.github.linyuzai.connection.loadbalance.core.message;

import com.github.linyuzai.connection.loadbalance.core.exception.ConnectionLoadBalanceException;

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
