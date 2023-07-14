package com.github.linyuzai.connection.loadbalance.core.message;

import com.github.linyuzai.connection.loadbalance.core.concept.ConnectionLoadBalanceConcept;
import com.github.linyuzai.connection.loadbalance.core.scope.Scoped;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.util.List;
import java.util.stream.Collectors;

/**
 * 消息工厂。
 * <p>
 * Factory of message.
 */
public interface MessageFactory extends Scoped {

    /**
     * 是否支持。
     * <p>
     * Use this factory to create message if return true.
     */
    boolean support(Object message, ConnectionLoadBalanceConcept concept);

    /**
     * 是否支持。
     * <p>
     * Use this factory to create message if return true.
     */
    default boolean support(Object message) {
        return support(message, null);
    }

    /**
     * 创建消息。
     * <p>
     * Create the message with input.
     */
    Message create(Object message, ConnectionLoadBalanceConcept concept);

    /**
     * 创建消息。
     * <p>
     * Create the message with input.
     */
    default Message create(Object message) {
        return create(message, null);
    }

    /**
     * 消息工厂代理。
     * <p>
     * Delegate of factory for message.
     */
    @Getter
    @RequiredArgsConstructor
    class Delegate implements MessageFactory {

        private final ConnectionLoadBalanceConcept concept;

        private final MessageFactory delegate;

        public static List<MessageFactory> delegate(ConnectionLoadBalanceConcept concept,
                                                    List<? extends MessageFactory> factories) {
            return factories.stream().map(it -> new Delegate(concept, it)).collect(Collectors.toList());
        }

        @Override
        public boolean support(Object message, ConnectionLoadBalanceConcept concept) {
            return delegate.support(message, concept);
        }

        @Override
        public boolean support(Object message) {
            return delegate.support(message, concept);
        }

        @Override
        public Message create(Object message, ConnectionLoadBalanceConcept concept) {
            return delegate.create(message, concept);
        }

        @Override
        public Message create(Object message) {
            return delegate.create(message, concept);
        }

        @Override
        public boolean support(String scope) {
            return delegate.support(scope);
        }
    }
}
