package com.github.linyuzai.connection.loadbalance.core.event;

import com.github.linyuzai.connection.loadbalance.core.concept.Connection;
import com.github.linyuzai.connection.loadbalance.core.message.PingMessage;
import lombok.Getter;

@Deprecated
@Getter
public class PingMessageEvent extends AbstractConnectionEvent {

    private final PingMessage message;

    public PingMessageEvent(Connection connection, PingMessage message) {
        super(connection);
        this.message = message;
    }
}
