package com.github.linyuzai.connection.loadbalance.core.select;

import com.github.linyuzai.connection.loadbalance.core.concept.Connection;
import com.github.linyuzai.connection.loadbalance.core.concept.ConnectionLoadBalanceConcept;
import com.github.linyuzai.connection.loadbalance.core.message.Message;
import com.github.linyuzai.connection.loadbalance.core.scope.Scoped;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 连接选择器。
 * 通过消息来筛选需要发送该消息的连接。
 * <p>
 * Selector to select connection by message.
 */
public interface ConnectionSelector extends Scoped {

    /**
     * 是否支持该消息。
     * <p>
     * If the selector is supported by the message.
     */
    boolean support(Message message, ConnectionLoadBalanceConcept concept);

    /**
     * 是否支持该消息。
     * <p>
     * If the selector is supported by the message.
     */
    default boolean support(Message message) {
        return support(message, null);
    }

    /**
     * 选择连接。
     * <p>
     * Select connections.
     */
    Collection<Connection> select(Message message, ConnectionLoadBalanceConcept concept);

    /**
     * 选择连接。
     * <p>
     * Select connections.
     */
    default Collection<Connection> select(Message message) {
        return select(message, null);
    }

    /**
     * 连接选择器代理。
     * <p>
     * Delegate of connection's selector.
     */
    @Getter
    @RequiredArgsConstructor
    class Delegate implements ConnectionSelector {

        private final ConnectionLoadBalanceConcept concept;

        private final ConnectionSelector delegate;

        public static List<ConnectionSelector> delegate(ConnectionLoadBalanceConcept concept,
                                                        List<? extends ConnectionSelector> selectors) {
            return selectors.stream().map(it -> new Delegate(concept, it)).collect(Collectors.toList());
        }

        @Override
        public boolean support(Message message, ConnectionLoadBalanceConcept concept) {
            return delegate.support(message, concept);
        }

        @Override
        public boolean support(Message message) {
            return delegate.support(message, concept);
        }

        @Override
        public Collection<Connection> select(Message message, ConnectionLoadBalanceConcept concept) {
            return delegate.select(message, concept);
        }

        @Override
        public Collection<Connection> select(Message message) {
            return delegate.select(message, concept);
        }

        @Override
        public boolean support(String scope) {
            return delegate.support(scope);
        }
    }
}
