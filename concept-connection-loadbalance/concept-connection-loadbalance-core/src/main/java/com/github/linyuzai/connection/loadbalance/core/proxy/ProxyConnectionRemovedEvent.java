package com.github.linyuzai.connection.loadbalance.core.proxy;

import com.github.linyuzai.connection.loadbalance.core.concept.Connection;
import com.github.linyuzai.connection.loadbalance.core.event.AbstractConnectionEvent;

public class ProxyConnectionRemovedEvent extends AbstractConnectionEvent implements ProxyConnectionEvent {

    public ProxyConnectionRemovedEvent(Connection connection) {
        super(connection);
    }
}
