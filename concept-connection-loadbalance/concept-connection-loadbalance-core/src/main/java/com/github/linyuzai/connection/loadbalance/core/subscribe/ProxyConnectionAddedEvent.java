package com.github.linyuzai.connection.loadbalance.core.subscribe;

import com.github.linyuzai.connection.loadbalance.core.concept.Connection;
import com.github.linyuzai.connection.loadbalance.core.event.AbstractConnectionEvent;

public class ProxyConnectionAddedEvent extends AbstractConnectionEvent implements ProxyConnectionEvent {

    public ProxyConnectionAddedEvent(Connection connection) {
        super(connection);
    }
}
