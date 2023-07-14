package com.github.linyuzai.connection.loadbalance.core.message;

import com.github.linyuzai.connection.loadbalance.core.concept.Connection;
import com.github.linyuzai.connection.loadbalance.core.message.decode.MessageDecoder;
import com.github.linyuzai.connection.loadbalance.core.message.encode.MessageEncoder;
import com.github.linyuzai.connection.loadbalance.core.scope.AbstractScoped;


/**
 * 消息编解码适配器的抽象类。
 * <p>
 * Abstract adapter of codec for message.
 */
public abstract class AbstractMessageCodecAdapter extends AbstractScoped implements MessageCodecAdapter {

    @Override
    public MessageEncoder getMessageEncoder(String type, MessageEncoder encoder) {
        switch (type) {
            case Connection.Type.CLIENT:
                return getClientMessageEncoder(encoder);
            case Connection.Type.SUBSCRIBER:
                return getSubscribeMessageEncoder(encoder);
            case Connection.Type.OBSERVABLE:
                return getForwardMessageEncoder(encoder);
            default:
                return getUndefinedTypeMessageEncoder(type, encoder);
        }
    }

    @Override
    public MessageDecoder getMessageDecoder(String type, MessageDecoder decoder) {
        switch (type) {
            case Connection.Type.CLIENT:
                return getClientMessageDecoder(decoder);
            case Connection.Type.SUBSCRIBER:
                return getForwardMessageDecoder(decoder);
            case Connection.Type.OBSERVABLE:
                return getSubscribeMessageDecoder(decoder);
            default:
                return getUndefinedTypeMessageDecoder(type, decoder);
        }
    }

    /**
     * 获得客户端连接的消息编码器。
     * <p>
     * Get encoder for message of client connection.
     */
    public MessageEncoder getClientMessageEncoder(MessageEncoder encoder) {
        return encoder;
    }

    /**
     * 获得客户端连接的消息解码器。
     * <p>
     * Get decoder for message of client connection.
     */
    public MessageDecoder getClientMessageDecoder(MessageDecoder decoder) {
        return decoder;
    }

    /**
     * 获得订阅连接的消息编码器。
     * <p>
     * Get encoder for message of subscription.
     */
    public MessageEncoder getSubscribeMessageEncoder(MessageEncoder encoder) {
        return encoder;
    }

    /**
     * 获得订阅连接的消息解码器。
     * <p>
     * Get decoder for message of subscription.
     */
    public MessageDecoder getSubscribeMessageDecoder(MessageDecoder decoder) {
        return decoder;
    }

    /**
     * 获得转发连接的消息编码器。
     * <p>
     * Get encoder for message of forward.
     */
    public MessageEncoder getForwardMessageEncoder(MessageEncoder encoder) {
        return encoder;
    }

    /**
     * 获得转发连接的消息解码器。
     * <p>
     * Get decoder for message of forward.
     */
    public MessageDecoder getForwardMessageDecoder(MessageDecoder decoder) {
        return decoder;
    }

    public MessageEncoder getUndefinedTypeMessageEncoder(String type, MessageEncoder encoder) {
        return encoder;
    }

    public MessageDecoder getUndefinedTypeMessageDecoder(String type, MessageDecoder decoder) {
        return decoder;
    }
}
