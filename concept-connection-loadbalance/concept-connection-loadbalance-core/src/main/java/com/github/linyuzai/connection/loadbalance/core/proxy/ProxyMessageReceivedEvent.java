package com.github.linyuzai.connection.loadbalance.core.proxy;

import com.github.linyuzai.connection.loadbalance.core.concept.Connection;
import com.github.linyuzai.connection.loadbalance.core.event.AbstractConnectionEvent;
import com.github.linyuzai.connection.loadbalance.core.message.Message;
import lombok.Getter;

@Getter
public class ProxyMessageReceivedEvent extends AbstractConnectionEvent implements ProxyConnectionEvent {

    private final Message message;

    public ProxyMessageReceivedEvent(Connection connection, Message message) {
        super(connection);
        this.message = message;
    }
}
