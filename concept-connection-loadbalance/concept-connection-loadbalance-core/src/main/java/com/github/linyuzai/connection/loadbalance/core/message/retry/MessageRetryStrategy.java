package com.github.linyuzai.connection.loadbalance.core.message.retry;

import com.github.linyuzai.connection.loadbalance.core.concept.ConnectionLoadBalanceConcept;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.util.function.Consumer;

public interface MessageRetryStrategy {

    void retry(Throwable e, Consumer<Consumer<Throwable>> retryable, Consumer<Throwable> error, ConnectionLoadBalanceConcept concept);

    default void retry(Throwable e, Consumer<Consumer<Throwable>> retryable, Consumer<Throwable> error) {
        retry(e, retryable, error, null);
    }

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
