package com.github.linyuzai.connection.loadbalance.core.event;

import com.github.linyuzai.connection.loadbalance.core.concept.Connection;
import com.github.linyuzai.connection.loadbalance.core.message.PongMessage;
import lombok.Getter;

@Getter
public class PongMessageEvent extends AbstractConnectionEvent {

    private final PongMessage message;

    public PongMessageEvent(Connection connection, PongMessage message) {
        super(connection);
        this.message = message;
    }
}
