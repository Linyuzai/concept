package com.github.linyuzai.connection.loadbalance.core.subscribe;

import com.github.linyuzai.connection.loadbalance.core.concept.Connection;
import com.github.linyuzai.connection.loadbalance.core.concept.ConnectionLoadBalanceConcept;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.util.function.Consumer;

/**
 * 连接订阅者
 * <p>
 * 可以理解为服务实例对其他服务的消息进行监听
 * <p>
 * 或是监听 Redis 和 MQ
 */
public interface ConnectionSubscriber {

    static Consumer<Connection> onSubscribeSuccess(ConnectionLoadBalanceConcept concept) {
        return concept::onEstablish;
    }

    static Consumer<Throwable> onSubscribeError(ConnectionLoadBalanceConcept concept) {
        return e -> concept.getEventPublisher().publish(new ConnectionSubscribeErrorEvent(e));
    }

    default void subscribe(ConnectionLoadBalanceConcept concept) {
        subscribe(onSubscribeSuccess(concept), onSubscribeError(concept), () -> {
        }, concept);
    }

    default void subscribe() {
        subscribe(null);
    }

    void subscribe(Consumer<Connection> onSuccess,
                   Consumer<Throwable> onError,
                   Runnable onComplete,
                   ConnectionLoadBalanceConcept concept);

    default void subscribe(Consumer<Connection> onSuccess,
                           Consumer<Throwable> onError,
                           Runnable onComplete) {
        subscribe(onSuccess, onError, onComplete, null);
    }

    @Getter
    @RequiredArgsConstructor
    class Delegate implements ConnectionSubscriber {

        private final ConnectionLoadBalanceConcept concept;

        private final ConnectionSubscriber delegate;

        public static ConnectionSubscriber delegate(ConnectionLoadBalanceConcept concept,
                                                    ConnectionSubscriber delegate) {
            return new Delegate(concept, delegate);
        }

        @Override
        public void subscribe(ConnectionLoadBalanceConcept concept) {
            delegate.subscribe(concept);
        }

        @Override
        public void subscribe() {
            delegate.subscribe(concept);
        }

        @Override
        public void subscribe(Consumer<Connection> onSuccess,
                              Consumer<Throwable> onError,
                              Runnable onComplete,
                              ConnectionLoadBalanceConcept concept) {
            delegate.subscribe(onSuccess, onError, onComplete, concept);
        }

        @Override
        public void subscribe(Consumer<Connection> onSuccess,
                              Consumer<Throwable> onError,
                              Runnable onComplete) {
            delegate.subscribe(onSuccess, onError, onComplete, concept);
        }
    }
}
