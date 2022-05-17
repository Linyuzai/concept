package com.github.linyuzai.connection.loadbalance.core.message;

import com.github.linyuzai.connection.loadbalance.core.concept.Connection;

public class MessageForwardHandler implements MessageReceiveEventListener {

    @Override
    public String getConnectionType() {
        return Connection.Type.SUBSCRIBER;
    }

    @Override
    public void onMessage(Message message, Connection connection) {
        try {
            connection.getConcept().send(message);
            connection.getConcept().publish(new MessageForwardEvent(connection, message));
        } catch (Throwable e) {
            connection.getConcept().publish(new MessageForwardErrorEvent(connection, message, e));
        }
    }
}
