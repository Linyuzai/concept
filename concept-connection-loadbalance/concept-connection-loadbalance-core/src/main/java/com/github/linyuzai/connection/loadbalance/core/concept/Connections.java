package com.github.linyuzai.connection.loadbalance.core.concept;

import lombok.AllArgsConstructor;

import java.util.Collection;

@AllArgsConstructor
public class Connections implements Connection {

    private Collection<Connection> connections;

    @Override
    public String host() {
        return null;
    }

    @Override
    public int port() {
        return -1;
    }

    @Override
    public void send(Object message) {
        connections.forEach(it -> it.send(message));
    }
}
