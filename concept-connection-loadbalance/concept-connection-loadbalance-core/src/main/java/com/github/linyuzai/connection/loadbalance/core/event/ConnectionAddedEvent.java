package com.github.linyuzai.connection.loadbalance.core.event;

import com.github.linyuzai.connection.loadbalance.core.concept.Connection;

public class ConnectionAddedEvent extends AbstractConnectionEvent {

    public ConnectionAddedEvent(Connection connection) {
        super(connection);
    }
}
