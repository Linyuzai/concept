package com.github.linyuzai.connection.loadbalance.netty.concept;

import com.github.linyuzai.connection.loadbalance.core.concept.AbstractConnectionLoadBalanceConcept;

public class NettyLoadBalanceConcept extends AbstractConnectionLoadBalanceConcept {

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
