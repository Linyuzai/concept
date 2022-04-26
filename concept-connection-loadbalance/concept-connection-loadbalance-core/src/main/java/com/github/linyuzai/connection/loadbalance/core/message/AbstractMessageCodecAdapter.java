package com.github.linyuzai.connection.loadbalance.core.message;

import com.github.linyuzai.connection.loadbalance.core.concept.Connection;
import com.github.linyuzai.connection.loadbalance.core.message.decode.MessageDecoder;
import com.github.linyuzai.connection.loadbalance.core.message.encode.MessageEncoder;

public abstract class AbstractMessageCodecAdapter implements MessageCodecAdapter {

    @Override
    public MessageEncoder getMessageEncoder(String type) {
        switch (type) {
            case Connection.Type.CLIENT:
                return getClientMessageEncoder();
            case Connection.Type.SUBSCRIBER:
                return getSubscribeMessageEncoder();
            case Connection.Type.OBSERVABLE:
                return getForwardMessageEncoder();
            default:
                return getUndefinedTypeMessageEncoder(type);
        }
    }

    @Override
    public MessageDecoder getMessageDecoder(String type) {
        switch (type) {
            case Connection.Type.CLIENT:
                return getClientMessageDecoder();
            case Connection.Type.SUBSCRIBER:
                return getForwardMessageDecoder();
            case Connection.Type.OBSERVABLE:
                return getSubscribeMessageDecoder();
            default:
                return getUndefinedTypeMessageDecoder(type);
        }
    }

    /**
     * 发消息给客户端时的消息编码器
     *
     * @return 消息编码器
     */
    public abstract MessageEncoder getClientMessageEncoder();

    /**
     * 接收客户端消息的消息解码器
     *
     * @return 消息解码器
     */
    public abstract MessageDecoder getClientMessageDecoder();

    /**
     * 订阅时发送服务信息的消息编码器
     *
     * @return 消息编码器
     */
    public abstract MessageEncoder getSubscribeMessageEncoder();

    /**
     * 订阅时接收服务信息的消息解码器
     *
     * @return 消息解码器
     */
    public abstract MessageDecoder getSubscribeMessageDecoder();

    /**
     * 转发消息的消息编码器
     *
     * @return 消息编码器
     */
    public abstract MessageEncoder getForwardMessageEncoder();

    /**
     * 接收消息转发的消息解码器
     *
     * @return 消息解码器
     */
    public abstract MessageDecoder getForwardMessageDecoder();

    public MessageEncoder getUndefinedTypeMessageEncoder(String type) {
        throw new UnsupportedOperationException();
    }

    public MessageDecoder getUndefinedTypeMessageDecoder(String type) {
        throw new UnsupportedOperationException();
    }
}
