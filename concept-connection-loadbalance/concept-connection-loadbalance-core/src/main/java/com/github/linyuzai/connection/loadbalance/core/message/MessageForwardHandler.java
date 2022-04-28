package com.github.linyuzai.connection.loadbalance.core.message;

import com.github.linyuzai.connection.loadbalance.core.concept.Connection;

public class MessageForwardHandler implements MessageReceiveEventListener {

    @Override
    public String getConnectionType() {
        return Connection.Type.SUBSCRIBER;
    }

    @Override
    public void onMessage(Message message, Connection connection) {
        connection.getConcept().send(message);
    }
}
