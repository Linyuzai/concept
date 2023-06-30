package com.github.linyuzai.connection.loadbalance.netty.concept;

import com.github.linyuzai.connection.loadbalance.core.concept.AbstractConnectionLoadBalanceConcept;
import lombok.AllArgsConstructor;

import java.util.ArrayList;

@AllArgsConstructor
public class NettyLoadBalanceConcept extends AbstractConnectionLoadBalanceConcept {

    private Builder builder;

    public Builder toBuilder() {
        return builder.copy();
    }

    public static class Builder extends AbstractBuilder<Builder, NettyLoadBalanceConcept> {

        protected Builder snapshot;

        @Override
        protected String getScope() {
            return NettyScoped.NAME;
        }

        @Override
        protected NettyLoadBalanceConcept create() {
            return new NettyLoadBalanceConcept(this.snapshot);
        }

        public Builder snapshot() {
            this.snapshot = copy();
            return this;
        }

        protected Builder copy() {
            Builder builder = new Builder();
            builder.connectionRepositoryFactories = new ArrayList<>(connectionRepositoryFactories);
            builder.connectionServerManagerFactories = new ArrayList<>(connectionServerManagerFactories);
            builder.connectionSubscriberFactories = new ArrayList<>(connectionSubscriberFactories);
            builder.connectionFactories = new ArrayList<>(connectionFactories);
            builder.connectionSelectors = new ArrayList<>(connectionSelectors);
            builder.messageFactories = new ArrayList<>(messageFactories);
            builder.messageCodecAdapters = new ArrayList<>(messageCodecAdapters);
            builder.messageRetryStrategyAdapters = new ArrayList<>(messageRetryStrategyAdapters);
            builder.messageIdempotentVerifierFactories = new ArrayList<>(messageIdempotentVerifierFactories);
            builder.scheduledExecutorFactories = new ArrayList<>(scheduledExecutorFactories);
            builder.eventPublisherFactories = new ArrayList<>(eventPublisherFactories);
            builder.eventListeners = new ArrayList<>(eventListeners);
            builder.snapshot = builder;
            return builder;
        }
    }
}
