package com.github.linyuzai.connection.loadbalance.core.message;

import com.github.linyuzai.connection.loadbalance.core.concept.ConnectionLoadBalanceConcept;
import com.github.linyuzai.connection.loadbalance.core.message.decode.MessageDecoder;
import com.github.linyuzai.connection.loadbalance.core.message.encode.MessageEncoder;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

/**
 * 消息编解码适配器
 * <p>
 * 统一管理编解码器，不然很混乱
 */
public interface MessageCodecAdapter {

    /**
     * 通过连接类型获得消息编码器
     *
     * @param type 连接类型
     * @return 消息编码器
     */
    MessageEncoder getMessageEncoder(String type, ConnectionLoadBalanceConcept concept);

    default MessageEncoder getMessageEncoder(String type) {
        return getMessageEncoder(type, null);
    }

    /**
     * 通过连接类型获得消息解码器
     *
     * @param type 连接类型
     * @return 消息解码器
     */
    MessageDecoder getMessageDecoder(String type, ConnectionLoadBalanceConcept concept);

    default MessageDecoder getMessageDecoder(String type) {
        return getMessageDecoder(type, null);
    }

    @Getter
    @RequiredArgsConstructor
    class Delegate implements MessageCodecAdapter {

        private final ConnectionLoadBalanceConcept concept;

        private final MessageCodecAdapter delegate;

        public static MessageCodecAdapter delegate(ConnectionLoadBalanceConcept concept,
                                                   MessageCodecAdapter delegate) {
            return new Delegate(concept, delegate);
        }

        @Override
        public MessageEncoder getMessageEncoder(String type, ConnectionLoadBalanceConcept concept) {
            return delegate.getMessageEncoder(type, concept);
        }

        @Override
        public MessageEncoder getMessageEncoder(String type) {
            return delegate.getMessageEncoder(type, concept);
        }

        @Override
        public MessageDecoder getMessageDecoder(String type, ConnectionLoadBalanceConcept concept) {
            return delegate.getMessageDecoder(type, concept);
        }

        @Override
        public MessageDecoder getMessageDecoder(String type) {
            return delegate.getMessageDecoder(type, concept);
        }
    }
}
