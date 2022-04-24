package com.github.linyuzai.connection.loadbalance.websocket.concept;

import com.github.linyuzai.connection.loadbalance.core.concept.AbstractConnection;
import com.github.linyuzai.connection.loadbalance.core.concept.Connection;
import com.github.linyuzai.connection.loadbalance.core.concept.ConnectionLoadBalanceConcept;
import com.github.linyuzai.connection.loadbalance.core.subscribe.AbstractConnectionSubscriber;
import com.github.linyuzai.connection.loadbalance.core.server.ConnectionServer;
import com.github.linyuzai.connection.loadbalance.core.subscribe.JacksonSubscribeMessageDecoder;
import com.github.linyuzai.connection.loadbalance.core.subscribe.JacksonSubscribeMessageEncoder;

import java.util.function.Consumer;

public abstract class WebSocketConnectionSubscriber extends AbstractConnectionSubscriber {

    public WebSocketConnectionSubscriber() {
        super("ws");
    }

    public WebSocketConnectionSubscriber(String protocol) {
        super(protocol);
    }

    @Override
    public void subscribe(ConnectionServer server, ConnectionLoadBalanceConcept concept, Consumer<Connection> consumer) {
        doSubscribe(server, (WebSocketLoadBalanceConcept) concept, consumer);
    }

    public abstract void doSubscribe(ConnectionServer server, WebSocketLoadBalanceConcept concept, Consumer<Connection> consumer);

    public void setDefaultMessageEncoder(AbstractConnection connection) {
        connection.setMessageEncoder(new JacksonSubscribeMessageEncoder());
    }

    public void setDefaultMessageDecoder(AbstractConnection connection) {
        connection.setMessageDecoder(new JacksonSubscribeMessageDecoder());
    }

    @Override
    public String getEndpointPrefix() {
        return WebSocketLoadBalanceConcept.SUBSCRIBER_ENDPOINT_PREFIX;
    }
}
