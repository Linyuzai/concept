package com.github.linyuzai.connection.loadbalance.core.concept;

import lombok.Getter;

@Getter
public class IdConnection extends AbstractConnection {

    private final Object id;

    public IdConnection(Object id, String type, ConnectionLoadBalanceConcept concept) {
        super(type);
        setConcept(concept);
        this.id = id;
    }

    @Override
    public void doSend(byte[] bytes) {

    }

    @Override
    public void close() {

    }
}
