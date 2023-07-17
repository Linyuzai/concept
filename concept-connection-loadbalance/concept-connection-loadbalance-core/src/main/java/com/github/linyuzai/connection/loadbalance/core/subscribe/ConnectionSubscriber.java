package com.github.linyuzai.connection.loadbalance.core.subscribe;

import com.github.linyuzai.connection.loadbalance.core.concept.Connection;
import com.github.linyuzai.connection.loadbalance.core.concept.ConnectionLoadBalanceConcept;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.util.function.Consumer;

/**
 * 连接订阅者。
 * 可以理解为服务实例对其他服务的消息进行监听。
 * <p>
 * Subscriber of connection.
 * It can be understood that the service instance listens to the messages of other services.
 */
public interface ConnectionSubscriber {

    /**
     * 订阅成功。
     * <p>
     * Subscribe success.
     */
    static Consumer<Connection> onSubscribeSuccess(ConnectionLoadBalanceConcept concept) {
        return concept::onEstablish;
    }

    /**
     * 订阅失败。
     * <p>
     * Subscribe error.
     */
    static Consumer<Throwable> onSubscribeError(ConnectionLoadBalanceConcept concept) {
        return e -> concept.getEventPublisher().publish(new ConnectionSubscribeErrorEvent(e));
    }

    /**
     * 订阅。
     * <p>
     * Subscribe.
     */
    default void subscribe(ConnectionLoadBalanceConcept concept) {
        subscribe(onSubscribeSuccess(concept), onSubscribeError(concept), () -> {
        }, concept);
    }

    /**
     * 订阅。
     * <p>
     * Subscribe.
     */
    default void subscribe() {
        subscribe(null);
    }

    /**
     * 订阅。
     * <p>
     * Subscribe.
     */
    void subscribe(Consumer<Connection> onSuccess,
                   Consumer<Throwable> onError,
                   Runnable onComplete,
                   ConnectionLoadBalanceConcept concept);

    /**
     * 订阅。
     * <p>
     * Subscribe.
     */
    default void subscribe(Consumer<Connection> onSuccess,
                           Consumer<Throwable> onError,
                           Runnable onComplete) {
        subscribe(onSuccess, onError, onComplete, null);
    }

    /**
     * 连接订阅器代理。
     * <p>
     * Delegate of connection subscriber.
     */
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
