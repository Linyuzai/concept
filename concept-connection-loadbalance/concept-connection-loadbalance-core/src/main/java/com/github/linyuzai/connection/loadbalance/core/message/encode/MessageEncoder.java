package com.github.linyuzai.connection.loadbalance.core.message.encode;

import com.github.linyuzai.connection.loadbalance.core.concept.ConnectionLoadBalanceConcept;
import com.github.linyuzai.connection.loadbalance.core.message.Message;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

/**
 * 消息编码器
 */
public interface MessageEncoder {

    Object encode(Message message, ConnectionLoadBalanceConcept concept);

    default Object encode(Message message) {
        return encode(message, null);
    }

    @Getter
    @RequiredArgsConstructor
    class Delegate implements MessageEncoder {

        private final ConnectionLoadBalanceConcept concept;

        private final MessageEncoder delegate;

        public static MessageEncoder delegate(ConnectionLoadBalanceConcept concept,
                                              MessageEncoder delegate) {
            return new Delegate(concept, delegate);
        }

        @Override
        public Object encode(Message message, ConnectionLoadBalanceConcept concept) {
            return delegate.encode(message, concept);
        }

        @Override
        public Object encode(Message message) {
            return delegate.encode(message, concept);
        }
    }
}
