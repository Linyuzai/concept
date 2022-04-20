package com.github.linyuzai.connection.loadbalance.core.event;

import com.github.linyuzai.connection.loadbalance.core.concept.Connection;

public class ConnectionCreatedEvent extends AbstractConnectionEvent {

    public ConnectionCreatedEvent(Connection connection) {
        super(connection);
    }
}
