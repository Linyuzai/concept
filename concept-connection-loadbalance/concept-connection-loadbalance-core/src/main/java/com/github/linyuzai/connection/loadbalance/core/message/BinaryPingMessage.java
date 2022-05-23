package com.github.linyuzai.connection.loadbalance.core.message;

import java.nio.ByteBuffer;

public class BinaryPingMessage extends BinaryMessage implements PingMessage {

    public BinaryPingMessage() {
        this(ByteBuffer.allocate(0));
    }

    public BinaryPingMessage(ByteBuffer payload) {
        super(payload);
    }
}
