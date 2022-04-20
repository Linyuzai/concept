package com.github.linyuzai.connection.loadbalance.core.proxy;

import com.github.linyuzai.connection.loadbalance.core.concept.Connection;
import com.github.linyuzai.connection.loadbalance.core.event.AbstractConnectionEvent;
import com.github.linyuzai.connection.loadbalance.core.server.ConnectionServer;
import lombok.Getter;

@Getter
public class ConnectionProxyEvent extends AbstractConnectionEvent {

    private final ConnectionServer connectionServer;

    public ConnectionProxyEvent(Connection connection, ConnectionServer connectionServer) {
        super(connection);
        this.connectionServer = connectionServer;
    }
}
