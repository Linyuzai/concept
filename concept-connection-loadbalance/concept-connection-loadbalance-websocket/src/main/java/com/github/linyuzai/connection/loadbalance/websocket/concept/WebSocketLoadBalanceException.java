package com.github.linyuzai.connection.loadbalance.websocket.concept;

import com.github.linyuzai.connection.loadbalance.core.exception.ConnectionLoadBalanceException;

public class WebSocketLoadBalanceException extends ConnectionLoadBalanceException {

    public WebSocketLoadBalanceException(String message) {
        super(message);
    }

    public WebSocketLoadBalanceException(String message, Throwable cause) {
        super(message, cause);
    }
}
