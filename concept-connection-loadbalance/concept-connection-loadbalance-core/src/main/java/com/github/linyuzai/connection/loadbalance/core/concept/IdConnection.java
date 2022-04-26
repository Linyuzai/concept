package com.github.linyuzai.connection.loadbalance.core.concept;

import lombok.Getter;

import java.net.URI;

@Getter
public class IdConnection extends AbstractConnection {

    private final Object id;

    public IdConnection(Object id, String type, ConnectionLoadBalanceConcept concept) {
        super(type);
        setConcept(concept);
        this.id = id;
    }

    @Override
    public void ping(Object ping) {

    }

    @Override
    public void pong(Object pong) {

    }

    @Override
    public void doSend(Object message) {

    }

    @Override
    public URI getUri() {
        return null;
    }

    @Override
    public void close() {

    }
}
