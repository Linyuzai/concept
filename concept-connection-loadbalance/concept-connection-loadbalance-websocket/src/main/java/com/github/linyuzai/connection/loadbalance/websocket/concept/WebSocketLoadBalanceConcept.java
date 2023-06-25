package com.github.linyuzai.connection.loadbalance.websocket.concept;

import com.github.linyuzai.connection.loadbalance.core.concept.AbstractConnectionLoadBalanceConcept;
import com.github.linyuzai.connection.loadbalance.core.event.ConnectionEventPublisher;
import com.github.linyuzai.connection.loadbalance.core.message.MessageCodecAdapter;
import com.github.linyuzai.connection.loadbalance.core.repository.ConnectionRepository;
import com.github.linyuzai.connection.loadbalance.core.server.ConnectionServerManager;
import com.github.linyuzai.connection.loadbalance.core.subscribe.ConnectionSubscriber;

/**
 * ws 负载均衡概念
 */
public class WebSocketLoadBalanceConcept extends AbstractConnectionLoadBalanceConcept {
    /**
     * 服务间订阅端点
     */
    public static final String SUBSCRIBER_ENDPOINT = "/concept-websocket-subscriber";
    /**
     * 默认服务端点的前缀
     */
    public static final String SERVER_ENDPOINT_PREFIX = "/concept-websocket/";

    public static String formatPrefix(String prefix) {
        StringBuilder builder = new StringBuilder();
        if (!prefix.startsWith("/")) {
            builder.append("/");
        }
        builder.append(prefix);
        if (!prefix.endsWith("/")) {
            builder.append("/");
        }
        return builder.toString();
    }

    private static WebSocketLoadBalanceConcept instance;

    public static WebSocketLoadBalanceConcept getInstance() {
        return instance;
    }

    public void holdInstance() {
        instance = this;
    }

    public static class Builder extends AbstractBuilder<Builder> {

        public Builder() {
            super(WebSocketScoped.NAME);
        }

        public WebSocketLoadBalanceConcept build() {
            init();
            WebSocketLoadBalanceConcept concept = new WebSocketLoadBalanceConcept();
            concept.setConnectionRepository(withScope(ConnectionRepository.class, connectionRepositoryFactories));
            concept.setConnectionServerManager(withScope(ConnectionServerManager.class, connectionServerManagerFactories));
            concept.setConnectionSubscriber(withScope(ConnectionSubscriber.class, connectionSubscriberFactories));
            concept.setConnectionFactories(withScope(connectionFactories));
            concept.setConnectionSelectors(withFilterChain(withScope(connectionSelectors)));
            concept.setMessageFactories(withScope(messageFactories));
            concept.setMessageCodecAdapter(withScope(MessageCodecAdapter.class, messageCodecAdapterFactories));
            concept.setEventPublisher(withScope(ConnectionEventPublisher.class, eventPublisherFactories, publisher ->
                    publisher.register(eventListeners)));
            return concept;
        }
    }
}
