package com.github.linyuzai.connection.loadbalance.core.message.decode;

import com.github.linyuzai.connection.loadbalance.core.message.BinaryMessage;
import com.github.linyuzai.connection.loadbalance.core.message.Message;

public class BinaryMessageDecoder implements MessageDecoder {

    @Override
    public Message decode(byte[] message) {
        return new BinaryMessage(message);
    }
}
