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

    void subscribe(Consumer<Connection> connectionConsumer,
                   Consumer<Throwable> errorConsumer,
                   ConnectionLoadBalanceConcept concept);

    default void subscribe(Consumer<Connection> connectionConsumer,
                           Consumer<Throwable> errorConsumer) {
        subscribe(connectionConsumer, errorConsumer, null);
    }

    MasterSlave getMasterSlave();

    enum MasterSlave {

        UNSUPPORTED, MASTER, SLAVE1
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
        public void subscribe(Consumer<Connection> connectionConsumer,
                              Consumer<Throwable> errorConsumer,
                              ConnectionLoadBalanceConcept concept) {
            delegate.subscribe(connectionConsumer, errorConsumer, concept);
        }

        @Override
        public void subscribe(Consumer<Connection> connectionConsumer,
                              Consumer<Throwable> errorConsumer) {
            delegate.subscribe(connectionConsumer, errorConsumer, concept);
        }

        @Override
        public MasterSlave getMasterSlave() {
            return delegate.getMasterSlave();
        }
    }
}
