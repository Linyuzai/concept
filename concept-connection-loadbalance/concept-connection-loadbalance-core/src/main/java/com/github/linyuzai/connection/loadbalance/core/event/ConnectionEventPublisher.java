package com.github.linyuzai.connection.loadbalance.core.event;

import com.github.linyuzai.connection.loadbalance.core.concept.ConnectionLoadBalanceConcept;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.util.Collection;
import java.util.List;

/**
 * 事件发布者。
 * <p>
 * Event publisher.
 */
public interface ConnectionEventPublisher {

    /**
     * 发布事件。
     * <p>
     * Publish event.
     */
    void publish(Object event, ConnectionLoadBalanceConcept concept);

    default void publish(Object event) {
        publish(event, null);
    }

    /**
     * 注册事件监听器。
     * <p>
     * Register event listener.
     */
    void register(ConnectionEventListener listener, ConnectionLoadBalanceConcept concept);

    default void register(ConnectionEventListener listener) {
        register(listener, null);
    }

    /**
     * 注册事件监听器。
     * <p>
     * Register event listeners.
     */
    void register(Collection<? extends ConnectionEventListener> listeners, ConnectionLoadBalanceConcept concept);

    default void register(Collection<? extends ConnectionEventListener> listeners) {
        register(listeners, null);
    }

    /**
     * 注销事件监听器。
     * <p>
     * Unregister event listener.
     */
    void unregister(ConnectionEventListener listener, ConnectionLoadBalanceConcept concept);

    default void unregister(ConnectionEventListener listener) {
        unregister(listener, null);
    }

    /**
     * 注销事件监听器。
     * <p>
     * Unregister event listeners.
     */
    void unregister(Collection<? extends ConnectionEventListener> listeners, ConnectionLoadBalanceConcept concept);

    default void unregister(Collection<? extends ConnectionEventListener> listeners) {
        unregister(listeners, null);
    }

    List<ConnectionEventListener> getListeners();

    /**
     * 事件发布者代理。
     * <p>
     * Delegate of event publisher.
     */
    @Getter
    @RequiredArgsConstructor
    class Delegate implements ConnectionEventPublisher {

        private final ConnectionLoadBalanceConcept concept;

        private final ConnectionEventPublisher delegate;

        public static ConnectionEventPublisher delegate(ConnectionLoadBalanceConcept concept,
                                                        ConnectionEventPublisher delegate) {
            return new Delegate(concept, delegate);
        }

        @Override
        public void publish(Object event, ConnectionLoadBalanceConcept concept) {
            delegate.publish(event, concept);
        }

        @Override
        public void publish(Object event) {
            delegate.publish(event, concept);
        }

        @Override
        public void register(ConnectionEventListener listener, ConnectionLoadBalanceConcept concept) {
            delegate.register(listener, concept);
        }

        @Override
        public void register(ConnectionEventListener listener) {
            delegate.register(listener, concept);
        }

        @Override
        public void register(Collection<? extends ConnectionEventListener> listeners, ConnectionLoadBalanceConcept concept) {
            delegate.register(listeners, concept);
        }

        @Override
        public void register(Collection<? extends ConnectionEventListener> listeners) {
            delegate.register(listeners, concept);
        }

        @Override
        public void unregister(ConnectionEventListener listener, ConnectionLoadBalanceConcept concept) {
            delegate.unregister(listener, concept);
        }

        @Override
        public void unregister(ConnectionEventListener listener) {
            delegate.unregister(listener, concept);
        }

        @Override
        public void unregister(Collection<? extends ConnectionEventListener> listeners, ConnectionLoadBalanceConcept concept) {
            delegate.unregister(listeners, concept);
        }

        @Override
        public void unregister(Collection<? extends ConnectionEventListener> listeners) {
            delegate.unregister(listeners, concept);
        }

        @Override
        public List<ConnectionEventListener> getListeners() {
            return delegate.getListeners();
        }
    }
}
