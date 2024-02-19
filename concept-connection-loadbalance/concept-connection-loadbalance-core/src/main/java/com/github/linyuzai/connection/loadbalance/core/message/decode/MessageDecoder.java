package com.github.linyuzai.connection.loadbalance.core.message.decode;

import com.github.linyuzai.connection.loadbalance.core.concept.Connection;
import com.github.linyuzai.connection.loadbalance.core.concept.ConnectionLoadBalanceConcept;
import com.github.linyuzai.connection.loadbalance.core.message.Message;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

/**
 * 消息解码器。
 * <p>
 * Message decoder.
 */
public interface MessageDecoder {

    /**
     * 解码。
     * <p>
     * Decode.
     */
    Message decode(Object message, Connection connection, ConnectionLoadBalanceConcept concept);

    /**
     * 解码。
     * <p>
     * Decode.
     */
    default Message decode(Object message, Connection connection) {
        return decode(message, connection, null);
    }

    /**
     * 消息解码器代理。
     * <p>
     * Delegate of message decoder.
     */
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
        public Message decode(Object message, Connection connection, ConnectionLoadBalanceConcept concept) {
            return delegate.decode(message, connection, concept);
        }

        @Override
        public Message decode(Object message, Connection connection) {
            return delegate.decode(message, connection, concept);
        }
    }
}
