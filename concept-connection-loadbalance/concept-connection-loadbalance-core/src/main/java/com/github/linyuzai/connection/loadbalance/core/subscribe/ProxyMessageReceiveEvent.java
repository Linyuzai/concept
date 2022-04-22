package com.github.linyuzai.connection.loadbalance.core.subscribe;

import com.github.linyuzai.connection.loadbalance.core.concept.Connection;
import com.github.linyuzai.connection.loadbalance.core.event.AbstractConnectionEvent;
import com.github.linyuzai.connection.loadbalance.core.message.Message;
import lombok.Getter;

@Getter
public class ProxyMessageReceiveEvent extends AbstractConnectionEvent implements ProxyConnectionEvent {

    private final Message message;

    public ProxyMessageReceiveEvent(Connection connection, Message message) {
        super(connection);
        this.message = message;
    }
}
