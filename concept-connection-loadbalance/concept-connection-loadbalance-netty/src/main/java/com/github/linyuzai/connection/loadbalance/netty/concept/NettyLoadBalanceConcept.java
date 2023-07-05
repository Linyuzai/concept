package com.github.linyuzai.connection.loadbalance.netty.concept;

import com.github.linyuzai.connection.loadbalance.core.concept.AbstractConnectionLoadBalanceConcept;
import com.github.linyuzai.connection.loadbalance.core.concept.Connection;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.util.ArrayList;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Consumer;

@RequiredArgsConstructor
public class NettyLoadBalanceConcept extends AbstractConnectionLoadBalanceConcept {

    @Getter
    private final Map<Object, NettyLoadBalanceConcept> concepts;

    private final Builder builder;

    public NettyLoadBalanceConcept createConcept(Object key) {
        return createConcept(key, unused -> {
        });
    }

    public NettyLoadBalanceConcept createConcept(Object key,
                                                 Consumer<Builder> consumer) {
        if (concepts.containsKey(key)) {
            throw new IllegalArgumentException("Key exist: " + key);
        }
        Builder duplicate = builder.duplicate();
        consumer.accept(duplicate);
        NettyLoadBalanceConcept concept = duplicate.build();
        concepts.put(key, concept);
        return concept;
    }

    @Override
    protected void onDestroy() {
        for (String type : connectionRepository.types()) {
            for (Connection connection : connectionRepository.select(type)) {
                connection.close();
            }
        }
    }

    public static class Builder extends AbstractBuilder<Builder, NettyLoadBalanceConcept> {

        protected Map<Object, NettyLoadBalanceConcept> conceptMap;

        protected Builder snapshot;

        @Override
        protected String getScope() {
            return NettyScoped.NAME;
        }

        @Override
        protected NettyLoadBalanceConcept create() {
            return new NettyLoadBalanceConcept(this.conceptMap, this.snapshot);
        }

        public Builder snapshot() {
            this.snapshot = duplicate();
            return this;
        }

        protected Builder duplicate() {
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
            if (conceptMap == null) {
                conceptMap = new ConcurrentHashMap<>();
            }
            builder.conceptMap = conceptMap;
            builder.snapshot = builder;
            return builder;
        }
    }
}
