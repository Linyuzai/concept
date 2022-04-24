package com.github.linyuzai.connection.loadbalance.core.event;

import com.github.linyuzai.connection.loadbalance.core.concept.Connection;
import lombok.Getter;

@Getter
public class ConnectionCloseEvent extends AbstractConnectionEvent {

    private final Object reason;

    public ConnectionCloseEvent(Connection connection, Object reason) {
        super(connection);
        this.reason = reason;
    }
}
