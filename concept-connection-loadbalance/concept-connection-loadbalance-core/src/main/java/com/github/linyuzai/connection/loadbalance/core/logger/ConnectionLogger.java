package com.github.linyuzai.connection.loadbalance.core.logger;

import com.github.linyuzai.connection.loadbalance.core.concept.ConnectionLoadBalanceConcept;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

/**
 * 连接日志。
 * <p>
 * Logger of connections.
 */
public interface ConnectionLogger {

    void info(String msg, ConnectionLoadBalanceConcept concept);

    default void info(String msg) {
        info(msg, null);
    }

    void error(String msg, Throwable e, ConnectionLoadBalanceConcept concept);

    default void error(String msg, Throwable e) {
        error(msg, e, null);
    }

    /**
     * 连接日志代理。
     * <p>
     * Delegate of logger.
     */
    @Getter
    @RequiredArgsConstructor
    class Delegate implements ConnectionLogger {

        private final ConnectionLoadBalanceConcept concept;

        private final ConnectionLogger delegate;

        public static ConnectionLogger delegate(ConnectionLoadBalanceConcept concept,
                                                ConnectionLogger delegate) {
            return new Delegate(concept, delegate);
        }

        @Override
        public void info(String msg, ConnectionLoadBalanceConcept concept) {
            delegate.info(msg, concept);
        }

        @Override
        public void info(String msg) {
            delegate.info(msg, concept);
        }

        @Override
        public void error(String msg, Throwable e, ConnectionLoadBalanceConcept concept) {
            delegate.error(msg, e, concept);
        }

        @Override
        public void error(String msg, Throwable e) {
            delegate.error(msg, e, concept);
        }
    }
}
