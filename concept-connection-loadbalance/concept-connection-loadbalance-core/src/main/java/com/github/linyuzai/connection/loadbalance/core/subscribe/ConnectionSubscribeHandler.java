package com.github.linyuzai.connection.loadbalance.core.subscribe;

import com.github.linyuzai.connection.loadbalance.core.concept.Connection;
import com.github.linyuzai.connection.loadbalance.core.event.ConnectionEventListener;
import com.github.linyuzai.connection.loadbalance.core.event.MessageReceiveEvent;
import com.github.linyuzai.connection.loadbalance.core.message.Message;

public class ConnectionSubscribeHandler implements ConnectionEventListener {

    @Override
    public void onEvent(Object event) {
        if (event instanceof MessageReceiveEvent) {
            Message message = ((MessageReceiveEvent) event).getMessage();
            Connection connection = ((MessageReceiveEvent) event).getConnection();
            if (Connection.Type.OBSERVABLE.equals(connection.getType())) {
                connection.getConcept().subscribe(message.getPayload(), false);
            }
        }
    }
}
