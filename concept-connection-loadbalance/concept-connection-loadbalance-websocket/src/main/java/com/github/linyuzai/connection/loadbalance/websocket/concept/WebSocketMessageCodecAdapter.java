package com.github.linyuzai.connection.loadbalance.websocket.concept;

import com.github.linyuzai.connection.loadbalance.core.message.AbstractMessageCodecAdapter;
import com.github.linyuzai.connection.loadbalance.core.message.decode.MessageDecoder;
import com.github.linyuzai.connection.loadbalance.core.message.encode.MessageEncoder;

public class WebSocketMessageCodecAdapter extends AbstractMessageCodecAdapter {

    @Override
    public MessageEncoder getClientMessageEncoder() {
        return null;
    }

    @Override
    public MessageDecoder getClientMessageDecoder() {
        return null;
    }

    @Override
    public MessageEncoder getSubscriberMessageEncoder() {
        return null;
    }

    @Override
    public MessageDecoder getSubscriberMessageDecoder() {
        return null;
    }

    @Override
    public MessageEncoder getObservableMessageEncoder() {
        return null;
    }

    @Override
    public MessageDecoder getObservableMessageDecoder() {
        return null;
    }
}
