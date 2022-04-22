package com.github.linyuzai.connection.loadbalance.websocket.concept;

import com.github.linyuzai.connection.loadbalance.core.concept.AbstractConnectionLoadBalanceConcept;
import com.github.linyuzai.connection.loadbalance.core.concept.ConnectionFactory;
import com.github.linyuzai.connection.loadbalance.core.event.ConnectionEventPublisher;
import com.github.linyuzai.connection.loadbalance.core.message.MessageFactory;
import com.github.linyuzai.connection.loadbalance.core.subscribe.ConnectionSubscriber;
import com.github.linyuzai.connection.loadbalance.core.select.ConnectionSelector;
import com.github.linyuzai.connection.loadbalance.core.server.ConnectionServerProvider;
import com.github.linyuzai.connection.loadbalance.websocket.javax.JavaxWebSocketConnectionFactory;
import com.github.linyuzai.connection.loadbalance.websocket.javax.JavaxProxyWebSocketConnectionFactory;

import java.util.List;

public class WebSocketLoadBalanceConcept extends AbstractConnectionLoadBalanceConcept {
    public static final String ENDPOINT_PREFIX = "/concept-websocket-loadbalance/";
    private static WebSocketLoadBalanceConcept instance;

    private WebSocketLoadBalanceConcept(ConnectionServerProvider connectionServerProvider, ConnectionSubscriber connectionSubscriber, List<ConnectionFactory> connectionFactories, List<ConnectionSelector> connectionSelectors, List<MessageFactory> messageFactories, ConnectionEventPublisher eventPublisher) {
        super(connectionServerProvider, connectionSubscriber, connectionFactories, connectionSelectors, messageFactories, eventPublisher);
        instance = this;
    }

    public static WebSocketLoadBalanceConcept getInstance() {
        return instance;
    }

    public static class Builder extends AbstractBuilder<Builder> {

        public WebSocketLoadBalanceConcept build() {
            preBuild();

            connectionFactories.add(new JavaxWebSocketConnectionFactory());
            connectionFactories.add(new JavaxProxyWebSocketConnectionFactory());

            return new WebSocketLoadBalanceConcept(
                    connectionServerProvider,
                    connectionSubscriber,
                    connectionFactories,
                    connectionSelectors,
                    messageFactories,
                    eventPublisher);
        }
    }
}
