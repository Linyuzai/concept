package com.github.linyuzai.connection.loadbalance.core.message;

import com.github.linyuzai.connection.loadbalance.core.message.decode.JacksonTextMessageDecoder;
import com.github.linyuzai.connection.loadbalance.core.message.decode.MessageDecoder;
import com.github.linyuzai.connection.loadbalance.core.message.decode.SimpleMessageDecoder;
import com.github.linyuzai.connection.loadbalance.core.message.encode.JacksonTextMessageEncoder;
import com.github.linyuzai.connection.loadbalance.core.message.encode.MessageEncoder;
import com.github.linyuzai.connection.loadbalance.core.subscribe.JacksonSubscribeMessageDecoder;

/**
 * 基础编解码适配器。
 * 提供最基础的编解码器。
 * <p>
 * Adapter for message codec to provider basic encoder and decoder.
 */
public class BaseMessageCodecAdapter extends AbstractMessageCodecAdapter {

    @Override
    public MessageEncoder getClientMessageEncoder(MessageEncoder encoder) {
        return new JacksonTextMessageEncoder();
    }

    @Override
    public MessageDecoder getClientMessageDecoder(MessageDecoder decoder) {
        return new SimpleMessageDecoder();
    }

    @Override
    public MessageEncoder getSubscribeMessageEncoder(MessageEncoder encoder) {
        return new JacksonTextMessageEncoder();
    }

    @Override
    public MessageDecoder getSubscribeMessageDecoder(MessageDecoder decoder) {
        return new JacksonSubscribeMessageDecoder();
    }

    @Override
    public MessageEncoder getForwardMessageEncoder(MessageEncoder encoder) {
        return new JacksonTextMessageEncoder(true);
    }

    @Override
    public MessageDecoder getForwardMessageDecoder(MessageDecoder decoder) {
        return new JacksonTextMessageDecoder();
    }
}
