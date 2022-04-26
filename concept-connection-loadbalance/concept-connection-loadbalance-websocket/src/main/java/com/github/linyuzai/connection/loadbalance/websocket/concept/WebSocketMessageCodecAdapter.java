package com.github.linyuzai.connection.loadbalance.websocket.concept;

import com.github.linyuzai.connection.loadbalance.core.message.AbstractMessageCodecAdapter;
import com.github.linyuzai.connection.loadbalance.core.message.decode.MessageDecoder;
import com.github.linyuzai.connection.loadbalance.core.message.decode.TextMessageDecoder;
import com.github.linyuzai.connection.loadbalance.core.message.encode.JacksonTextMessageEncoder;
import com.github.linyuzai.connection.loadbalance.core.message.encode.MessageEncoder;
import com.github.linyuzai.connection.loadbalance.core.subscribe.JacksonForwardMessageDecoder;
import com.github.linyuzai.connection.loadbalance.core.subscribe.JacksonForwardMessageEncoder;
import com.github.linyuzai.connection.loadbalance.core.subscribe.JacksonSubscribeMessageDecoder;
import com.github.linyuzai.connection.loadbalance.core.subscribe.JacksonSubscribeMessageEncoder;

public class WebSocketMessageCodecAdapter extends AbstractMessageCodecAdapter {

    @Override
    public MessageEncoder getClientMessageEncoder() {
        return new JacksonTextMessageEncoder();
    }

    @Override
    public MessageDecoder getClientMessageDecoder() {
        return new TextMessageDecoder();
    }

    @Override
    public MessageEncoder getSubscriberMessageEncoder() {
        return new JacksonSubscribeMessageEncoder();
    }

    @Override
    public MessageDecoder getSubscriberMessageDecoder() {
        return new JacksonForwardMessageDecoder();
    }

    @Override
    public MessageEncoder getObservableMessageEncoder() {
        return new JacksonForwardMessageEncoder();
    }

    @Override
    public MessageDecoder getObservableMessageDecoder() {
        return new JacksonSubscribeMessageDecoder();
    }
}
