package com.github.linyuzai.connection.loadbalance.core.event;

import com.github.linyuzai.connection.loadbalance.core.concept.Connection;
import com.github.linyuzai.connection.loadbalance.core.concept.IdConnection;
import lombok.Getter;

@Getter
public class UnknownErrorEvent implements ConnectionEvent {

    private final Connection connection;

    private final Throwable error;

    public UnknownErrorEvent(Object id, String type, Throwable e) {
        this.connection = new IdConnection(id, type);
        this.error = e;
    }
}
