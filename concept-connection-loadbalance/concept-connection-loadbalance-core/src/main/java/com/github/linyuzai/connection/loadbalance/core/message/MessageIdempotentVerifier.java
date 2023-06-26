package com.github.linyuzai.connection.loadbalance.core.message;

import com.github.linyuzai.connection.loadbalance.core.concept.ConnectionLoadBalanceConcept;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

public interface MessageIdempotentVerifier {

    MessageIdempotentVerifier VERIFIED = (message, concept) -> true;

    boolean verify(Message message, ConnectionLoadBalanceConcept concept);

    default boolean verify(Message message) {
        return verify(message, null);
    }

    @Getter
    @RequiredArgsConstructor
    class Delegate implements MessageIdempotentVerifier {

        private final ConnectionLoadBalanceConcept concept;

        private final MessageIdempotentVerifier delegate;

        public static MessageIdempotentVerifier delegate(ConnectionLoadBalanceConcept concept,
                                                         MessageIdempotentVerifier delegate) {
            return new Delegate(concept, delegate);
        }

        @Override
        public boolean verify(Message message, ConnectionLoadBalanceConcept concept) {
            return delegate.verify(message, concept);
        }

        @Override
        public boolean verify(Message message) {
            return delegate.verify(message, concept);
        }
    }
}
