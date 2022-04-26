package com.github.linyuzai.connection.loadbalance.websocket.concept;

import com.github.linyuzai.connection.loadbalance.core.server.ConnectionServer;
import com.github.linyuzai.connection.loadbalance.core.subscribe.AbstractConnectionSubscriber;

import java.net.URI;
import java.util.function.Consumer;

public abstract class WebSocketConnectionSubscriber<T extends WebSocketConnection>
        extends AbstractConnectionSubscriber<T, WebSocketLoadBalanceConcept> {

    public WebSocketConnectionSubscriber() {
        super("ws");
    }

    public WebSocketConnectionSubscriber(String protocol) {
        super(protocol);
    }

    @Override
    public void doSubscribe(ConnectionServer server, WebSocketLoadBalanceConcept concept, Consumer<T> consumer) {
        URI uri = getUri(server);
        doSubscribe(uri, concept, connection -> {
            connection.getMetadata().put(ConnectionServer.class, server);
            consumer.accept(connection);
        });
    }

    public abstract void doSubscribe(URI uri, WebSocketLoadBalanceConcept concept, Consumer<T> consumer);

    @Override
    public String getEndpointPrefix() {
        return WebSocketLoadBalanceConcept.SUBSCRIBER_ENDPOINT;
    }
}
