package com.github.linyuzai.connection.loadbalance.core.select;

import com.github.linyuzai.connection.loadbalance.core.concept.Connection;
import com.github.linyuzai.connection.loadbalance.core.concept.ConnectionLoadBalanceConcept;
import com.github.linyuzai.connection.loadbalance.core.message.Message;
import com.github.linyuzai.connection.loadbalance.core.repository.ConnectionRepository;
import com.github.linyuzai.connection.loadbalance.core.scope.Scoped;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 连接选择器
 * <p>
 * 通过消息来筛选需要发送该消息的连接
 */
public interface ConnectionSelector extends Scoped {

    @Override
    default boolean support(String scope) {
        return true;
    }

    /**
     * 是否支持该消息
     *
     * @param message 消息
     * @return 是否支持
     */
    boolean support(Message message, ConnectionLoadBalanceConcept concept);

    default boolean support(Message message) {
        return support(message, null);
    }

    /**
     * 选择连接
     *
     * @param message 消息
     * @param concept {@link ConnectionLoadBalanceConcept}
     * @return 需要发送该消息的连接
     */
    Collection<Connection> select(Message message, ConnectionLoadBalanceConcept concept);

    default Collection<Connection> select(Message message) {
        return select(message, null);
    }

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
