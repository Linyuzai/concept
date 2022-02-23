package com.github.linyuzai.connection.loadbalance.core.exception;

public class ConnectionLoadBalanceException extends RuntimeException {

    public ConnectionLoadBalanceException(String message) {
        super(message);
    }

    public ConnectionLoadBalanceException(String message, Throwable cause) {
        super(message, cause);
    }
}
