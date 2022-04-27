package com.github.linyuzai.connection.loadbalance.core.message;

import java.nio.ByteBuffer;

public class BinaryMessage extends AbstractMessage<ByteBuffer> {

    public BinaryMessage(ByteBuffer payload) {
        super(payload);
    }
}
