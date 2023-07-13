package com.github.linyuzai.connection.loadbalance.core.message.retry;

import com.github.linyuzai.connection.loadbalance.core.concept.ConnectionLoadBalanceConcept;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.util.function.Consumer;

/**
 * 消息重试策略。
 * 不适用于心跳。
 * <p>
 * Retry strategy for message sending which not applicable to heartbeat.
 */
public interface MessageRetryStrategy {

    /**
     * 重试。
     * <p>
     * Retry.
     */
    void retry(Throwable e, Consumer<Consumer<Throwable>> retryable, Consumer<Throwable> error, ConnectionLoadBalanceConcept concept);

    /**
     * 重试。
     * <p>
     * Retry.
     */
    default void retry(Throwable e, Consumer<Consumer<Throwable>> retryable, Consumer<Throwable> error) {
        retry(e, retryable, error, null);
    }

    /**
     * 消息重试策略代理。
     * <p>
     * Delegate of retry strategy for message sending.
     */
    @Getter
    @RequiredArgsConstructor
    class Delegate implements MessageRetryStrategy {

        private final ConnectionLoadBalanceConcept concept;

        private final MessageRetryStrategy delegate;

        public static MessageRetryStrategy delegate(ConnectionLoadBalanceConcept concept,
                                                    MessageRetryStrategy delegate) {
            return new Delegate(concept, delegate);
        }

        @Override
        public void retry(Throwable e, Consumer<Consumer<Throwable>> retryable, Consumer<Throwable> error, ConnectionLoadBalanceConcept concept) {
            delegate.retry(e, retryable, error, concept);
        }

        @Override
        public void retry(Throwable e, Consumer<Consumer<Throwable>> retryable, Consumer<Throwable> error) {
            delegate.retry(e, retryable, error, concept);
        }
    }
}
