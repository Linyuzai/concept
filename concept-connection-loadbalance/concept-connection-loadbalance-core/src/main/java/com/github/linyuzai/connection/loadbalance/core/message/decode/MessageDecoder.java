package com.github.linyuzai.connection.loadbalance.core.message.decode;

import com.github.linyuzai.connection.loadbalance.core.concept.ConnectionLoadBalanceConcept;
import com.github.linyuzai.connection.loadbalance.core.message.Message;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

/**
 * 消息解码器
 */
public interface MessageDecoder {

    Message decode(Object message, ConnectionLoadBalanceConcept concept);

    default Message decode(Object message) {
        return decode(message, null);
    }

    @Getter
    @RequiredArgsConstructor
    class Delegate implements MessageDecoder {

        private final ConnectionLoadBalanceConcept concept;

        private final MessageDecoder delegate;

        public static MessageDecoder delegate(ConnectionLoadBalanceConcept concept,
                                              MessageDecoder delegate) {
            return new Delegate(concept, delegate);
        }

        @Override
        public Message decode(Object message, ConnectionLoadBalanceConcept concept) {
            return delegate.decode(message, concept);
        }

        @Override
        public Message decode(Object message) {
            return delegate.decode(message, concept);
        }
    }
}
