package com.github.linyuzai.connection.loadbalance.core.subscribe;

import com.github.linyuzai.connection.loadbalance.core.concept.Connection;
import com.github.linyuzai.connection.loadbalance.core.event.AbstractConnectionEvent;

public class ProxyConnectionCloseEvent extends AbstractConnectionEvent implements ProxyConnectionEvent {

    public ProxyConnectionCloseEvent(Connection connection) {
        super(connection);
    }
}
