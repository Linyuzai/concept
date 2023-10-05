package com.github.linyuzai.connection.loadbalance.core.message.encode;

import com.github.linyuzai.connection.loadbalance.core.concept.Connection;
import com.github.linyuzai.connection.loadbalance.core.concept.ConnectionLoadBalanceConcept;
import com.github.linyuzai.connection.loadbalance.core.message.Message;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

/**
 * 消息编码器。
 * <p>
 * Message encoder.
 */
public interface MessageEncoder {

    /**
     * 编码。
     * <p>
     * Encode.
     */
    Object encode(Message message, Connection connection, ConnectionLoadBalanceConcept concept);

    @Deprecated
    default Object encode(Message message, ConnectionLoadBalanceConcept concept) {
        return encode(message, null, concept);
    }

    /**
     * 编码。
     * <p>
     * Encode.
     */
    default Object encode(Message message, Connection connection) {
        return encode(message, connection, null);
    }

    @Deprecated
    default Object encode(Message message) {
        return encode(message, null, null);
    }

    /**
     * 消息编码器代理。
     * <p>
     * Delegate of message encoder.
     */
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
        public Object encode(Message message, Connection connection, ConnectionLoadBalanceConcept concept) {
            return delegate.encode(message, connection, concept);
        }

        @Deprecated
        @Override
        public Object encode(Message message, ConnectionLoadBalanceConcept concept) {
            return delegate.encode(message, concept);
        }

        @Override
        public Object encode(Message message, Connection connection) {
            return delegate.encode(message, connection, concept);
        }

        @Deprecated
        @Override
        public Object encode(Message message) {
            return delegate.encode(message, concept);
        }
    }
}
