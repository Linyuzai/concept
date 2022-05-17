package com.github.linyuzai.connection.loadbalance.core.subscribe;

import com.github.linyuzai.connection.loadbalance.core.concept.Connection;
import com.github.linyuzai.connection.loadbalance.core.message.Message;
import com.github.linyuzai.connection.loadbalance.core.message.MessageReceiveEventListener;
import com.github.linyuzai.connection.loadbalance.core.server.ConnectionServer;

public class ConnectionSubscribeHandler implements MessageReceiveEventListener {

    @Override
    public String getConnectionType() {
        return Connection.Type.OBSERVABLE;
    }

    @Override
    public void onMessage(Message message, Connection connection) {
        ConnectionServer server = message.getPayload();
        connection.getMetadata().put(ConnectionServer.class, server);
        connection.getConcept().subscribe(server, true);
    }
}
