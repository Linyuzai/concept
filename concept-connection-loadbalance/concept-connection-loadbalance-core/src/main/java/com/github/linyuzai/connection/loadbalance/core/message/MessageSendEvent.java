package com.github.linyuzai.connection.loadbalance.core.message;

import com.github.linyuzai.connection.loadbalance.core.concept.Connection;
import com.github.linyuzai.connection.loadbalance.core.event.AbstractConnectionEvent;
import lombok.Getter;

import java.util.Collection;

@Getter
public class MessageSendEvent implements MessageEvent {

    private final Message message;

    private final Collection<Connection> connections;

    public MessageSendEvent(Message message, Collection<Connection> connections) {
        this.message = message;
        this.connections = connections;
    }
}
