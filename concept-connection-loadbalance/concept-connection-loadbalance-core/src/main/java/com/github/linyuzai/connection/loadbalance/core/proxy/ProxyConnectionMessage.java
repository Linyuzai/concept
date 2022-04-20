package com.github.linyuzai.connection.loadbalance.core.proxy;

import com.github.linyuzai.connection.loadbalance.core.message.AbstractMessage;
import com.github.linyuzai.connection.loadbalance.core.server.ConnectionServer;

public class ProxyConnectionMessage extends AbstractMessage {

    private final ConnectionServer server;

    public ProxyConnectionMessage(ConnectionServer server) {
        this.server = server;
        getHeaders().put(ProxyMarker.FLAG, "connection-server");
    }

    @SuppressWarnings("unchecked")
    @Override
    public ConnectionServer getPayload() {
        return server;
    }
}
