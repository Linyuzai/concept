package com.github.linyuzai.connection.loadbalance.core.subscribe;

import com.github.linyuzai.connection.loadbalance.core.concept.ConnectionLoadBalanceConcept;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

/**
 * 连接订阅者
 * <p>
 * 可以理解为服务实例对其他服务的消息进行监听
 * <p>
 * 或是监听 Redis 和 MQ
 */
public interface ConnectionSubscriber {

    void subscribe(ConnectionLoadBalanceConcept concept);

    default void subscribe() {
        subscribe(null);
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
    }
}
