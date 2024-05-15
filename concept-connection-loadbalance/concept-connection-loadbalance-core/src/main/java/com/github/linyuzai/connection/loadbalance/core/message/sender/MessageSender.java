package com.github.linyuzai.connection.loadbalance.core.message.sender;

import com.github.linyuzai.connection.loadbalance.core.concept.ConnectionLoadBalanceConcept;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.util.Collection;

/**
 * 消息发送器。
 * 用于自定义发送策略。
 * <p>
 * Message sender.
 * Send message with some strategy.
 */
public interface MessageSender {

    /**
     * 发送消息。
     * <p>
     * Send message.
     */
    void send(Collection<? extends Runnable> senders, ConnectionLoadBalanceConcept concept);

    /**
     * 发送消息。
     * <p>
     * Send message.
     */
    default void send(Collection<? extends Runnable> senders) {
        send(senders, null);
    }

    /**
     * 消息发送器代理。
     * <p>
     * Delegate of message sender.
     */
    @Getter
    @RequiredArgsConstructor
    class Delegate implements MessageSender {

        private final ConnectionLoadBalanceConcept concept;

        private final MessageSender delegate;

        public static MessageSender delegate(ConnectionLoadBalanceConcept concept,
                                             MessageSender delegate) {
            return new Delegate(concept, delegate);
        }

        @Override
        public void send(Collection<? extends Runnable> senders, ConnectionLoadBalanceConcept concept) {
            delegate.send(senders, concept);
        }

        @Override
        public void send(Collection<? extends Runnable> senders) {
            delegate.send(senders, concept);
        }
    }
}
