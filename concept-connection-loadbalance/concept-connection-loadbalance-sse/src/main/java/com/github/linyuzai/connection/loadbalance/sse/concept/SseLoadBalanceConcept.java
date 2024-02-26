package com.github.linyuzai.connection.loadbalance.sse.concept;

import com.github.linyuzai.connection.loadbalance.core.concept.AbstractConnectionLoadBalanceConcept;

/**
 * SSE 负载均衡概念。
 * <p>
 * SSE load balance concept.
 */
public class SseLoadBalanceConcept extends AbstractConnectionLoadBalanceConcept {

    public static final String ID = "sse";

    @Override
    public String getId() {
        return ID;
    }

    public static class Builder extends AbstractBuilder<Builder, SseLoadBalanceConcept> {

        @Override
        protected String getScope() {
            return SseScoped.NAME;
        }

        @Override
        protected SseLoadBalanceConcept create() {
            return new SseLoadBalanceConcept();
        }
    }
}
