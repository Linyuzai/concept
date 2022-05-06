package com.github.linyuzai.connection.loadbalance.websocket.concept;

import com.github.linyuzai.connection.loadbalance.core.message.AbstractMessageCodecAdapter;
import com.github.linyuzai.connection.loadbalance.core.message.decode.MessageDecoder;
import com.github.linyuzai.connection.loadbalance.core.message.decode.SampleMessageDecoder;
import com.github.linyuzai.connection.loadbalance.core.message.encode.JacksonTextMessageEncoder;
import com.github.linyuzai.connection.loadbalance.core.message.encode.MessageEncoder;
import com.github.linyuzai.connection.loadbalance.core.subscribe.JacksonSubscribeMessageDecoder;

public class WebSocketMessageCodecAdapter extends AbstractMessageCodecAdapter {

    @Override
    public MessageEncoder getClientMessageEncoder() {
        return new JacksonTextMessageEncoder();
    }

    @Override
    public MessageDecoder getClientMessageDecoder() {
        return new SampleMessageDecoder();
    }

    @Override
    public MessageEncoder getSubscribeMessageEncoder() {
        return new JacksonTextMessageEncoder();
    }

    @Override
    public MessageDecoder getSubscribeMessageDecoder() {
        return new JacksonSubscribeMessageDecoder();
    }

    @Override
    public MessageEncoder getForwardMessageEncoder() {
        return new JacksonTextMessageEncoder(true);
    }

    @Override
    public MessageDecoder getForwardMessageDecoder() {
        return new SampleMessageDecoder();
    }
}
