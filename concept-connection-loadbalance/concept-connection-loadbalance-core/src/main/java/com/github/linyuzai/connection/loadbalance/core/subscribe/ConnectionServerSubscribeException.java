package com.github.linyuzai.connection.loadbalance.core.subscribe;

import com.github.linyuzai.connection.loadbalance.core.exception.ConnectionLoadBalanceException;
import com.github.linyuzai.connection.loadbalance.core.server.ConnectionServer;
import lombok.Getter;

/**
 * 连接订阅异常。
 * <p>
 * Connection subscribe exception.
 */
@Getter
public class ConnectionServerSubscribeException extends ConnectionLoadBalanceException {

    private final ConnectionServer connectionServer;

    public ConnectionServerSubscribeException(ConnectionServer connectionServer, String message) {
        super(message);
        this.connectionServer = connectionServer;
    }

    public ConnectionServerSubscribeException(ConnectionServer connectionServer, String message, Throwable cause) {
        super(message, cause);
        this.connectionServer = connectionServer;
    }
}
