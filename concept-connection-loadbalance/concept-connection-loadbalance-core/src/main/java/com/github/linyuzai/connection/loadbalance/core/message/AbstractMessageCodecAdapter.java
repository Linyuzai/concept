package com.github.linyuzai.connection.loadbalance.core.message;

import com.github.linyuzai.connection.loadbalance.core.concept.Connection;
import com.github.linyuzai.connection.loadbalance.core.message.decode.MessageDecoder;
import com.github.linyuzai.connection.loadbalance.core.message.encode.MessageEncoder;
import com.github.linyuzai.connection.loadbalance.core.scope.AbstractScoped;

/**
 * 消息编解码适配器的抽象类
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
     * 发消息给客户端时的消息编码器
     *
     * @return 消息编码器
     */
    public MessageEncoder getClientMessageEncoder(MessageEncoder encoder) {
        return encoder;
    }

    /**
     * 接收客户端消息的消息解码器
     *
     * @return 消息解码器
     */
    public MessageDecoder getClientMessageDecoder(MessageDecoder decoder) {
        return decoder;
    }

    /**
     * 订阅时发送服务信息的消息编码器
     *
     * @return 消息编码器
     */
    public MessageEncoder getSubscribeMessageEncoder(MessageEncoder encoder) {
        return encoder;
    }

    /**
     * 订阅时接收服务信息的消息解码器
     *
     * @return 消息解码器
     */
    public MessageDecoder getSubscribeMessageDecoder(MessageDecoder decoder) {
        return decoder;
    }

    /**
     * 转发消息的消息编码器
     *
     * @return 消息编码器
     */
    public MessageEncoder getForwardMessageEncoder(MessageEncoder encoder) {
        return encoder;
    }

    /**
     * 接收消息转发的消息解码器
     *
     * @return 消息解码器
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
