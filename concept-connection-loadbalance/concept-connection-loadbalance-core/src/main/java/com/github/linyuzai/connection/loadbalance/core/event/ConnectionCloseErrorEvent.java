package com.github.linyuzai.connection.loadbalance.core.event;

import com.github.linyuzai.connection.loadbalance.core.concept.Connection;
import lombok.Getter;

@Getter
public class ConnectionCloseErrorEvent extends AbstractConnectionEvent {

    private final Throwable error;

    public ConnectionCloseErrorEvent(Connection connection, Throwable e) {
        super(connection);
        this.error = e;
    }
}
