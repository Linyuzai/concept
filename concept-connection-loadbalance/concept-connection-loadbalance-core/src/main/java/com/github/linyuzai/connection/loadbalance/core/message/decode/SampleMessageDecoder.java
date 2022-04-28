package com.github.linyuzai.connection.loadbalance.core.message.decode;

import com.github.linyuzai.connection.loadbalance.core.message.BinaryMessage;
import com.github.linyuzai.connection.loadbalance.core.message.Message;
import com.github.linyuzai.connection.loadbalance.core.message.TextMessage;

import java.nio.ByteBuffer;

public class SampleMessageDecoder implements MessageDecoder {

    @Override
    public Message decode(Object message) {
        if (message instanceof String) {
            return new TextMessage((String) message);
        } else if (message instanceof ByteBuffer) {
            return new BinaryMessage((ByteBuffer) message);
        } else if (message instanceof byte[]) {
            return new BinaryMessage(ByteBuffer.wrap((byte[]) message));
        } else {
            throw new MessageDecodeException(message);
        }
    }
}
