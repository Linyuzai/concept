package com.github.linyuzai.connection.loadbalance.core.message;

import java.nio.ByteBuffer;

public class BinaryPongMessage extends BinaryMessage implements PongMessage {

    public BinaryPongMessage() {
        this(ByteBuffer.allocate(0));
    }

    public BinaryPongMessage(ByteBuffer payload) {
        super(payload);
    }
}
