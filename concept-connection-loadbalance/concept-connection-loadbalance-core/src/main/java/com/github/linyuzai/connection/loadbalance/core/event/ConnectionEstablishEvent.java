package com.github.linyuzai.connection.loadbalance.core.event;

import com.github.linyuzai.connection.loadbalance.core.concept.Connection;

/**
 * 连接建立事件
 */
public class ConnectionEstablishEvent extends AbstractConnectionEvent {

    public ConnectionEstablishEvent(Connection connection) {
        super(connection);
    }
}
