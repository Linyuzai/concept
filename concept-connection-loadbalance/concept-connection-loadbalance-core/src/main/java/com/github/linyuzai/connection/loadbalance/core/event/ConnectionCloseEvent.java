package com.github.linyuzai.connection.loadbalance.core.event;

import com.github.linyuzai.connection.loadbalance.core.concept.Connection;

public class ConnectionCloseEvent extends AbstractConnectionEvent {

    public ConnectionCloseEvent(Connection connection) {
        super(connection);
    }
}
