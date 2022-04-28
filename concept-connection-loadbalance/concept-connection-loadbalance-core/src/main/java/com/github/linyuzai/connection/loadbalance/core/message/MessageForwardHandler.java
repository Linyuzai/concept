package com.github.linyuzai.connection.loadbalance.core.message;

import com.github.linyuzai.connection.loadbalance.core.concept.Connection;
import com.github.linyuzai.connection.loadbalance.core.event.ConnectionEventListener;
import com.github.linyuzai.connection.loadbalance.core.event.MessageReceiveEvent;

public class MessageForwardHandler implements ConnectionEventListener {

    @Override
    public void onEvent(Object event) {
        if (event instanceof MessageReceiveEvent) {
            Message message = ((MessageReceiveEvent) event).getMessage();
            Connection connection = ((MessageReceiveEvent) event).getConnection();
            if (Connection.Type.SUBSCRIBER.equals(connection.getType())) {
                connection.getConcept().send(message);
            }
        }
    }
}
