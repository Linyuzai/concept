package com.github.linyuzai.connection.loadbalance.websocket.concept;

import com.github.linyuzai.connection.loadbalance.core.concept.AbstractConnectionLoadBalanceConcept;
import com.github.linyuzai.connection.loadbalance.core.concept.ConnectionFactory;
import com.github.linyuzai.connection.loadbalance.core.event.ConnectionEventPublisher;
import com.github.linyuzai.connection.loadbalance.core.message.MessageCodecAdapter;
import com.github.linyuzai.connection.loadbalance.core.message.MessageFactory;
import com.github.linyuzai.connection.loadbalance.core.select.ConnectionSelector;
import com.github.linyuzai.connection.loadbalance.core.server.ConnectionServerProvider;
import com.github.linyuzai.connection.loadbalance.core.subscribe.ConnectionSubscriber;
import com.github.linyuzai.connection.loadbalance.websocket.javax.JavaxWebSocketConnectionFactory;

import java.util.List;

public class WebSocketLoadBalanceConcept extends AbstractConnectionLoadBalanceConcept {
    public static final String SUBSCRIBER_ENDPOINT = "/concept-websocket-subscriber";
    public static final String SERVER_ENDPOINT_PREFIX = "/concept-websocket/";
    private static WebSocketLoadBalanceConcept instance;

    private WebSocketLoadBalanceConcept(ConnectionServerProvider connectionServerProvider,
                                        ConnectionSubscriber connectionSubscriber,
                                        List<ConnectionFactory> connectionFactories,
                                        List<ConnectionSelector> connectionSelectors,
                                        MessageCodecAdapter messageCodecAdapter,
                                        List<MessageFactory> messageFactories,
                                        ConnectionEventPublisher eventPublisher) {
        super(connectionServerProvider, connectionSubscriber, connectionFactories,
                connectionSelectors, messageFactories, messageCodecAdapter, eventPublisher);
    }

    public static WebSocketLoadBalanceConcept getInstance() {
        return instance;
    }

    public void holdInstance() {
        instance = this;
    }

    public static class Builder extends AbstractBuilder<Builder> {

        public WebSocketLoadBalanceConcept build() {

            if (messageCodecAdapter == null) {
                messageCodecAdapter = new WebSocketMessageCodecAdapter();
            }

            preBuild();

            connectionFactories.add(new JavaxWebSocketConnectionFactory());

            return new WebSocketLoadBalanceConcept(
                    connectionServerProvider,
                    connectionSubscriber,
                    connectionFactories,
                    connectionSelectors,
                    messageCodecAdapter,
                    messageFactories,
                    eventPublisher);
        }
    }
}
