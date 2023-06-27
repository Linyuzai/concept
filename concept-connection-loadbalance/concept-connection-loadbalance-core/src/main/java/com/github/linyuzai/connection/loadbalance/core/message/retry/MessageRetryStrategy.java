package com.github.linyuzai.connection.loadbalance.core.message.retry;

import com.github.linyuzai.connection.loadbalance.core.concept.ConnectionLoadBalanceConcept;
import com.github.linyuzai.connection.loadbalance.core.message.MessageSendErrorEvent;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

public interface MessageRetryStrategy {

    void retry(Runnable retryable, MessageSendErrorEvent event, ConnectionLoadBalanceConcept concept);

    default void retry(Runnable retryable, MessageSendErrorEvent event) {
        retry(retryable, event, null);
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
        public void retry(Runnable retryable, MessageSendErrorEvent event, ConnectionLoadBalanceConcept concept) {
            delegate.retry(retryable, event, concept);
        }

        @Override
        public void retry(Runnable retryable, MessageSendErrorEvent event) {
            delegate.retry(retryable, event, concept);
        }
    }
}
