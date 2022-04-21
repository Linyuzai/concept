package com.github.linyuzai.connection.loadbalance.core.event;

import com.github.linyuzai.connection.loadbalance.core.concept.Connection;
import com.github.linyuzai.connection.loadbalance.core.message.Message;
import lombok.Getter;

@Getter
public class MessageReceiveEvent extends AbstractConnectionEvent {

    private final Message message;

    public MessageReceiveEvent(Connection connection, Message message) {
        super(connection);
        this.message = message;
    }
}
