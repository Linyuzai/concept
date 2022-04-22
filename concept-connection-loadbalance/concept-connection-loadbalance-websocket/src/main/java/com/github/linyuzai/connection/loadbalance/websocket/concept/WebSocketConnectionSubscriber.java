package com.github.linyuzai.connection.loadbalance.websocket.concept;

import com.github.linyuzai.connection.loadbalance.core.concept.Connection;
import com.github.linyuzai.connection.loadbalance.core.concept.ConnectionLoadBalanceConcept;
import com.github.linyuzai.connection.loadbalance.core.subscribe.AbstractConnectionSubscriber;
import com.github.linyuzai.connection.loadbalance.core.server.ConnectionServer;
import lombok.NoArgsConstructor;

@NoArgsConstructor
public abstract class WebSocketConnectionSubscriber extends AbstractConnectionSubscriber {

    public WebSocketConnectionSubscriber(String protocol) {
        super(protocol);
    }

    @Override
    public Connection subscribe(ConnectionServer server, ConnectionLoadBalanceConcept concept) {
        return doSubscribe(server, (WebSocketLoadBalanceConcept) concept);
    }

    public abstract Connection doSubscribe(ConnectionServer server, WebSocketLoadBalanceConcept concept);

    @Override
    public String getEndpointPrefix() {
        return WebSocketLoadBalanceConcept.ENDPOINT_PREFIX;
    }
}
