package com.github.linyuzai.connection.loadbalance.core.message;

import com.github.linyuzai.connection.loadbalance.core.concept.Connection;
import com.github.linyuzai.connection.loadbalance.core.concept.ConnectionLoadBalanceConcept;
import com.github.linyuzai.connection.loadbalance.core.message.decode.JacksonTextMessageDecoder;
import com.github.linyuzai.connection.loadbalance.core.message.decode.MessageDecoder;
import com.github.linyuzai.connection.loadbalance.core.message.decode.SampleMessageDecoder;
import com.github.linyuzai.connection.loadbalance.core.message.encode.JacksonTextMessageEncoder;
import com.github.linyuzai.connection.loadbalance.core.message.encode.MessageEncoder;
import com.github.linyuzai.connection.loadbalance.core.subscribe.JacksonSubscribeMessageDecoder;

/**
 * 消息编解码适配器的抽象类
 */
public abstract class AbstractMessageCodecAdapter implements MessageCodecAdapter {

    @Override
    public MessageEncoder getMessageEncoder(String type, ConnectionLoadBalanceConcept concept) {
        switch (type) {
            case Connection.Type.CLIENT:
                return getClientMessageEncoder(concept);
            case Connection.Type.SUBSCRIBER:
                return getSubscribeMessageEncoder(concept);
            case Connection.Type.OBSERVABLE:
                return getForwardMessageEncoder(concept);
            default:
                return getUndefinedTypeMessageEncoder(type, concept);
        }
    }

    @Override
    public MessageDecoder getMessageDecoder(String type, ConnectionLoadBalanceConcept concept) {
        switch (type) {
            case Connection.Type.CLIENT:
                return getClientMessageDecoder(concept);
            case Connection.Type.SUBSCRIBER:
                return getForwardMessageDecoder(concept);
            case Connection.Type.OBSERVABLE:
                return getSubscribeMessageDecoder(concept);
            default:
                return getUndefinedTypeMessageDecoder(type, concept);
        }
    }

    /**
     * 发消息给客户端时的消息编码器
     *
     * @return 消息编码器
     */
    public MessageEncoder getClientMessageEncoder(ConnectionLoadBalanceConcept concept) {
        return new JacksonTextMessageEncoder();
    }

    /**
     * 接收客户端消息的消息解码器
     *
     * @return 消息解码器
     */
    public MessageDecoder getClientMessageDecoder(ConnectionLoadBalanceConcept concept) {
        return new SampleMessageDecoder();
    }

    /**
     * 订阅时发送服务信息的消息编码器
     *
     * @return 消息编码器
     */
    public MessageEncoder getSubscribeMessageEncoder(ConnectionLoadBalanceConcept concept) {
        return new JacksonTextMessageEncoder();
    }

    /**
     * 订阅时接收服务信息的消息解码器
     *
     * @return 消息解码器
     */
    public MessageDecoder getSubscribeMessageDecoder(ConnectionLoadBalanceConcept concept) {
        return new JacksonSubscribeMessageDecoder();
    }

    /**
     * 转发消息的消息编码器
     *
     * @return 消息编码器
     */
    public MessageEncoder getForwardMessageEncoder(ConnectionLoadBalanceConcept concept) {
        return new JacksonTextMessageEncoder(true);
    }

    /**
     * 接收消息转发的消息解码器
     *
     * @return 消息解码器
     */
    public MessageDecoder getForwardMessageDecoder(ConnectionLoadBalanceConcept concept) {
        return new JacksonTextMessageDecoder();
    }

    public MessageEncoder getUndefinedTypeMessageEncoder(String type, ConnectionLoadBalanceConcept concept) {
        throw new UnsupportedOperationException();
    }

    public MessageDecoder getUndefinedTypeMessageDecoder(String type, ConnectionLoadBalanceConcept concept) {
        throw new UnsupportedOperationException();
    }
}
