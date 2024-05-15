package com.github.linyuzai.connection.loadbalance.websocket.concept;

import com.github.linyuzai.connection.loadbalance.core.concept.AbstractConnectionLoadBalanceConcept;
import lombok.Getter;

/**
 * ws 负载均衡概念。
 * <p>
 * ws load balance concept.
 */
public class WebSocketLoadBalanceConcept extends AbstractConnectionLoadBalanceConcept {

    public static final String ID = "websocket";

    /**
     * 服务间订阅端点
     */
    public static final String SUBSCRIBER_ENDPOINT = "/concept-websocket-subscriber";

    /**
     * 默认服务端点的前缀
     */
    public static final String SERVER_ENDPOINT_PREFIX = "/concept-websocket/";

    @Getter
    private static WebSocketLoadBalanceConcept instance;

    public void holdInstance() {
        instance = this;
    }

    @Override
    public String getId() {
        return ID;
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
