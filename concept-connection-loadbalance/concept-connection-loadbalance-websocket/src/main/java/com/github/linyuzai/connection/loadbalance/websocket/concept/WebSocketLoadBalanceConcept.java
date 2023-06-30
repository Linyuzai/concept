package com.github.linyuzai.connection.loadbalance.websocket.concept;

import com.github.linyuzai.connection.loadbalance.core.concept.AbstractConnectionLoadBalanceConcept;

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

    public static class Builder extends AbstractBuilder<Builder, WebSocketLoadBalanceConcept> {

        @Override
        protected String getScope() {
            return WebSocketScoped.NAME;
        }

        @Override
        protected WebSocketLoadBalanceConcept create() {
            return new WebSocketLoadBalanceConcept();
        }
    }
}
