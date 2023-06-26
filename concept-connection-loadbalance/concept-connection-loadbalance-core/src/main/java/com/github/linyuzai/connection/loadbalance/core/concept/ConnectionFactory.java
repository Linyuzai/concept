package com.github.linyuzai.connection.loadbalance.core.concept;

import com.github.linyuzai.connection.loadbalance.core.scope.Scoped;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * 连接工厂
 * <p>
 * 用于将底层连接包装成 {@link Connection} 实例
 */
public interface ConnectionFactory extends Scoped {

    /**
     * 是否支持
     *
     * @param o        底层连接
     * @param metadata 元数据
     * @return 是否支持
     */
    boolean support(Object o, Map<Object, Object> metadata, ConnectionLoadBalanceConcept concept);

    default boolean support(Object o, Map<Object, Object> metadata) {
        return support(o, metadata, null);
    }

    /**
     * 创建 {@link Connection} 对象
     *
     * @param o        底层连接
     * @param metadata 元数据
     * @param concept  {@link ConnectionLoadBalanceConcept}
     * @return {@link Connection} 实例
     */
    Connection create(Object o, Map<Object, Object> metadata, ConnectionLoadBalanceConcept concept);

    default Connection create(Object o, Map<Object, Object> metadata) {
        return create(o, metadata, null);
    }

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
