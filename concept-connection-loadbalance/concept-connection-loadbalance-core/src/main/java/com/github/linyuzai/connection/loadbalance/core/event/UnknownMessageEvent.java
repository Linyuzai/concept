package com.github.linyuzai.connection.loadbalance.core.event;

import com.github.linyuzai.connection.loadbalance.core.concept.Connection;
import com.github.linyuzai.connection.loadbalance.core.concept.IdConnection;
import lombok.Getter;

@Getter
public class UnknownMessageEvent implements ConnectionEvent {

    private final Connection connection;

    private final byte[] message;

    public UnknownMessageEvent(Object id, byte[] message) {
        this.connection = new IdConnection(id);
        this.message = message;
    }
}
