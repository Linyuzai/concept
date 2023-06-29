package com.github.linyuzai.connection.loadbalance.core.message;

import com.github.linyuzai.connection.loadbalance.core.message.decode.JacksonTextMessageDecoder;
import com.github.linyuzai.connection.loadbalance.core.message.decode.MessageDecoder;
import com.github.linyuzai.connection.loadbalance.core.message.decode.SampleMessageDecoder;
import com.github.linyuzai.connection.loadbalance.core.message.encode.JacksonTextMessageEncoder;
import com.github.linyuzai.connection.loadbalance.core.message.encode.MessageEncoder;
import com.github.linyuzai.connection.loadbalance.core.subscribe.JacksonSubscribeMessageDecoder;

public class AnyMessageCodecAdapter extends AbstractMessageCodecAdapter {

    @Override
    public MessageEncoder getClientMessageEncoder(MessageEncoder encoder) {
        return new JacksonTextMessageEncoder();
    }

    @Override
    public MessageDecoder getClientMessageDecoder(MessageDecoder decoder) {
        return new SampleMessageDecoder();
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
