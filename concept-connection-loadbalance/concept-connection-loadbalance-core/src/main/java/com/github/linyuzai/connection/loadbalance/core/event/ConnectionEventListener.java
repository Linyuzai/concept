package com.github.linyuzai.connection.loadbalance.core.event;

import com.github.linyuzai.connection.loadbalance.core.concept.ConnectionLoadBalanceConcept;
import com.github.linyuzai.connection.loadbalance.core.scope.Scoped;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.util.List;
import java.util.stream.Collectors;

/**
 * 事件监听器
 */
public interface ConnectionEventListener extends Scoped {

    /**
     * 事件监听回调
     *
     * @param event 事件
     */
    void onEvent(Object event);

    default void onEvent(Object event, ConnectionLoadBalanceConcept concept) {
        onEvent(event);
    }

    @Getter
    @RequiredArgsConstructor
    class Delegate implements ConnectionEventListener {

        private final ConnectionLoadBalanceConcept concept;

        private final ConnectionEventListener delegate;

        public static List<ConnectionEventListener> delegate(ConnectionLoadBalanceConcept concept,
                                                             List<? extends ConnectionEventListener> listeners) {
            return listeners.stream().map(it -> new Delegate(concept, it)).collect(Collectors.toList());
        }

        @Override
        public void onEvent(Object event) {
            delegate.onEvent(event, concept);
        }

        @Override
        public void onEvent(Object event, ConnectionLoadBalanceConcept concept) {
            delegate.onEvent(event, concept);
        }

        @Override
        public boolean support(String scope) {
            return delegate.support(scope);
        }
    }
}
