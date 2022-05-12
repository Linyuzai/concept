package com.github.linyuzai.connection.loadbalance.core.concept;

import com.github.linyuzai.connection.loadbalance.core.message.PingMessage;
import com.github.linyuzai.connection.loadbalance.core.message.PongMessage;
import lombok.Getter;

import java.io.IOException;
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
    public void ping(PingMessage ping) {

    }

    @Override
    public void pong(PongMessage pong) {

    }

    @Override
    public void doSend(Object message) {

    }

    @Override
    public URI getUri() {
        return null;
    }

    @Override
    public void doClose(String reason) throws IOException {

    }
}
