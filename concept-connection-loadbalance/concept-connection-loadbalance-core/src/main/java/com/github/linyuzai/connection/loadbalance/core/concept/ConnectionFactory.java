package com.github.linyuzai.connection.loadbalance.core.concept;

import com.github.linyuzai.connection.loadbalance.core.scope.Scoped;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * 连接工厂。
 * <p>
 * Factory of connection.
 */
public interface ConnectionFactory extends Scoped {

    /**
     * 是否支持。
     * <p>
     * If supported.
     */
    boolean support(Object o, Map<Object, Object> metadata, ConnectionLoadBalanceConcept concept);

    /**
     * 是否支持。
     * <p>
     * If supported.
     */
    default boolean support(Object o, Map<Object, Object> metadata) {
        return support(o, metadata, null);
    }

    /**
     * 创建连接。
     * <p>
     * Create connection.
     */
    Connection create(Object o, Map<Object, Object> metadata, ConnectionLoadBalanceConcept concept);

    /**
     * 创建连接。
     * <p>
     * Create connection.
     */
    default Connection create(Object o, Map<Object, Object> metadata) {
        return create(o, metadata, null);
    }

    /**
     * 连接工厂代理。
     * <p>
     * Delegate of connection factory.
     */
    @Getter
    @RequiredArgsConstructor
    class Delegate implements ConnectionFactory {

        private final ConnectionLoadBalanceConcept concept;

        private final ConnectionFactory delegate;

        public static List<ConnectionFactory> delegate(ConnectionLoadBalanceConcept concept,
                                                       List<? extends ConnectionFactory> factories) {
            return factories.stream().map(it -> new Delegate(concept, it)).collect(Collectors.toList());
        }

        @Override
        public boolean support(Object o, Map<Object, Object> metadata, ConnectionLoadBalanceConcept concept) {
            return delegate.support(o, metadata, concept);
        }

        @Override
        public boolean support(Object o, Map<Object, Object> metadata) {
            return delegate.support(o, metadata, concept);
        }

        @Override
        public Connection create(Object o, Map<Object, Object> metadata, ConnectionLoadBalanceConcept concept) {
            return delegate.create(o, metadata, concept);
        }

        @Override
        public Connection create(Object o, Map<Object, Object> metadata) {
            return delegate.create(o, metadata, concept);
        }

        @Override
        public boolean support(String scope) {
            return delegate.support(scope);
        }
    }
}
