package com.github.linyuzai.connection.loadbalance.netty.concept;

import com.github.linyuzai.connection.loadbalance.core.concept.AbstractConnectionLoadBalanceConcept;
import com.github.linyuzai.connection.loadbalance.core.concept.Connection;
import lombok.RequiredArgsConstructor;

/**
 * Netty 负载均衡概念。
 * <p>
 * Netty load balance concept.
 */
@RequiredArgsConstructor
public class NettyLoadBalanceConcept extends AbstractConnectionLoadBalanceConcept {

    public static final String ID = "netty";

    @Override
    public String getId() {
        return ID;
    }

    public static class Builder extends AbstractBuilder<Builder, NettyLoadBalanceConcept> {

        @Override
        protected String getScope() {
            return NettyScoped.NAME;
        }

        @Override
        protected NettyLoadBalanceConcept create() {
            return new NettyLoadBalanceConcept();
        }
    }
}
