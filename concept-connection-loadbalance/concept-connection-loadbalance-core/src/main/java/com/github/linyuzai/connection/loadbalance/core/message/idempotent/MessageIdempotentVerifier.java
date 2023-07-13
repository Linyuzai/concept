package com.github.linyuzai.connection.loadbalance.core.message.idempotent;

import com.github.linyuzai.connection.loadbalance.core.concept.ConnectionLoadBalanceConcept;
import com.github.linyuzai.connection.loadbalance.core.message.Message;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.util.UUID;

/**
 * 消息幂等校验器。
 * <p>
 * Verify idempotent of message.
 */
public interface MessageIdempotentVerifier {

    MessageIdempotentVerifier VERIFIED = (message, concept) -> true;

    /**
     * 生成 message id。
     * <p>
     * Generate message id.
     */
    default String generateMessageId(Message message, ConnectionLoadBalanceConcept concept) {
        return UUID.randomUUID().toString();
    }

    /**
     * 生成 message id。
     * <p>
     * Generate message id.
     */
    default String generateMessageId(Message message) {
        return generateMessageId(message, null);
    }

    /**
     * 校验消息幂等。
     * 返回 true，通过校验；返回 false，消息重复。
     * <p>
     * Verify idempotent.
     * Return true if message id is not duplicate, false otherwise.
     */
    boolean verify(Message message, ConnectionLoadBalanceConcept concept);

    /**
     * 校验消息幂等。
     * 返回 true，通过校验；返回 false，消息重复。
     * <p>
     * Verify idempotent.
     * Return true if message id is not duplicate, false otherwise.
     */
    default boolean verify(Message message) {
        return verify(message, null);
    }

    /**
     * 消息幂等校验器代理。
     * <p>
     * Delegate of idempotent verifier.
     */
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
