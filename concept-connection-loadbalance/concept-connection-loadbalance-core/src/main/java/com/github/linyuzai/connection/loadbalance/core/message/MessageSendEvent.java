package com.github.linyuzai.connection.loadbalance.core.message;

import com.github.linyuzai.connection.loadbalance.core.concept.Connection;
import com.github.linyuzai.connection.loadbalance.core.event.AbstractConnectionEvent;
import lombok.Getter;

@Getter
public class MessageSendEvent extends AbstractConnectionEvent implements MessageEvent {

    private final Message message;

    public MessageSendEvent(Connection connection, Message message) {
        super(connection);
        this.message = message;
    }
}
