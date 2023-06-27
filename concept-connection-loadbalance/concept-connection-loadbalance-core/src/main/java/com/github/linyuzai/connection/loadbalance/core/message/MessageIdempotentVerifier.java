package com.github.linyuzai.connection.loadbalance.core.message;

import com.github.linyuzai.connection.loadbalance.core.concept.ConnectionLoadBalanceConcept;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.util.UUID;

public interface MessageIdempotentVerifier {

    MessageIdempotentVerifier VERIFIED = (message, concept) -> true;

    default String generateMessageId(Message message, ConnectionLoadBalanceConcept concept) {
        return UUID.randomUUID().toString();
    }

    default String generateMessageId(Message message) {
        return generateMessageId(message, null);
    }

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
        public String generateMessageId(Message message, ConnectionLoadBalanceConcept concept) {
            return delegate.generateMessageId(message, concept);
        }

        @Override
        public String generateMessageId(Message message) {
            return delegate.generateMessageId(message, concept);
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
