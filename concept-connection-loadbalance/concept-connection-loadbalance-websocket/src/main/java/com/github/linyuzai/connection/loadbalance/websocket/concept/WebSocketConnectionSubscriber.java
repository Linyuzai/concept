package com.github.linyuzai.connection.loadbalance.websocket.concept;

import com.github.linyuzai.connection.loadbalance.core.concept.Connection;
import com.github.linyuzai.connection.loadbalance.core.message.MessageCodecAdapter;
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
        T connection = doSubscribe(uri, concept);
        configureConnection(connection, server, concept);
        consumer.accept(connection);
    }

    public void configureConnection(T connection, ConnectionServer server, WebSocketLoadBalanceConcept concept) {
        connection.getMetadata().put(ConnectionServer.class, server);
        MessageCodecAdapter adapter = concept.getMessageCodecAdapter();
        connection.setMessageEncoder(adapter.getMessageEncoder(Connection.Type.SUBSCRIBER));
        connection.setMessageDecoder(adapter.getMessageDecoder(Connection.Type.SUBSCRIBER));
    }

    public abstract T doSubscribe(URI uri, WebSocketLoadBalanceConcept concept);

    @Override
    public String getEndpointPrefix() {
        return WebSocketLoadBalanceConcept.SUBSCRIBER_ENDPOINT;
    }
}
