package com.github.linyuzai.connection.loadbalance.core.message.encode;

import com.github.linyuzai.connection.loadbalance.core.message.BinaryMessage;
import com.github.linyuzai.connection.loadbalance.core.message.Message;

import java.nio.ByteBuffer;

public class JacksonForwardMessageEncoder extends JacksonMessageEncoder {

    @Override
    public Object getPayload(Message message) {
        Object payload = message.getPayload();
        if (payload instanceof byte[]) {
            message.getHeaders().put(Message.BINARY, Boolean.TRUE.toString());
        } else if (payload instanceof ByteBuffer) {
            message.getHeaders().put(Message.BINARY, Boolean.TRUE.toString());
            BinaryMessage binaryMessage = new BinaryMessage();
            binaryMessage.setHeaders(message.getHeaders());
            binaryMessage.setPayload(((ByteBuffer) payload).array());
            return binaryMessage;
        }
        return message;
    }
}
