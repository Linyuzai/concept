package com.github.linyuzai.connection.loadbalance.core.subscribe;

import com.github.linyuzai.connection.loadbalance.core.concept.Connection;
import com.github.linyuzai.connection.loadbalance.core.event.AbstractConnectionEvent;
import com.github.linyuzai.connection.loadbalance.core.server.ConnectionServer;
import lombok.Getter;

@Getter
public class ProxyConnectionOpenEvent extends AbstractConnectionEvent implements ProxyConnectionEvent {

    private final ConnectionServer connectionServer;

    public ProxyConnectionOpenEvent(Connection connection, ConnectionServer connectionServer) {
        super(connection);
        this.connectionServer = connectionServer;
    }
}
