package com.github.linyuzai.connection.loadbalance.core.event;

import com.github.linyuzai.connection.loadbalance.core.concept.Connection;

public class ConnectionOpenEvent extends AbstractConnectionEvent {

    public ConnectionOpenEvent(Connection connection) {
        super(connection);
    }
}
