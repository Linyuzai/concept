package com.github.linyuzai.connection.loadbalance.core.message;

import java.nio.ByteBuffer;

public interface PingMessage extends Message {

    @SuppressWarnings("unchecked")
    @Override
    ByteBuffer getPayload();
}
