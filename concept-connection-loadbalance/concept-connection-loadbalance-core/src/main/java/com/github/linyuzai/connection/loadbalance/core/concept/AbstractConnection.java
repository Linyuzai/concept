package com.github.linyuzai.connection.loadbalance.core.concept;

import com.github.linyuzai.connection.loadbalance.core.message.decode.MessageDecoder;
import com.github.linyuzai.connection.loadbalance.core.message.encode.MessageEncoder;
import com.github.linyuzai.connection.loadbalance.core.message.Message;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.util.Map;

@Getter
@RequiredArgsConstructor
public abstract class AbstractConnection implements Connection {

    private final Map<String, String> metadata;

    private final MessageEncoder messageEncoder;

    private final MessageDecoder messageDecoder;

    @Override
    public void send(Message message) {
        MessageEncoder encoder = getMessageEncoder();
        byte[] bytes = encoder.encode(message);
        doSend(bytes);
    }

    public abstract void doSend(byte[] bytes);
}
