package com.github.linyuzai.connection.loadbalance.core.concept;

import com.github.linyuzai.connection.loadbalance.core.message.Message;
import lombok.AllArgsConstructor;

import java.util.Collection;

@AllArgsConstructor
public class Connections implements Connection {

    private Collection<? extends Connection> connections;

    public Collection<? extends Connection> real() {
        return connections;
    }

    @Override
    public void send(Message message) {
        connections.forEach(it -> it.send(message));
    }
}
