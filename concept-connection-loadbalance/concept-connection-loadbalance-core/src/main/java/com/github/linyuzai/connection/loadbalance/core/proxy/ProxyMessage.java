package com.github.linyuzai.connection.loadbalance.core.proxy;

import com.github.linyuzai.connection.loadbalance.core.message.AbstractMessage;
import com.github.linyuzai.connection.loadbalance.core.server.ConnectionServer;

public class ProxyMessage extends AbstractMessage {

    private final ConnectionServer server;

    public ProxyMessage(ConnectionServer server) {
        this.server = server;
        getHeaders().put(ConnectionProxy.HEADER, "connection-server");
    }

    @SuppressWarnings("unchecked")
    @Override
    public ConnectionServer getPayload() {
        return server;
    }
}
