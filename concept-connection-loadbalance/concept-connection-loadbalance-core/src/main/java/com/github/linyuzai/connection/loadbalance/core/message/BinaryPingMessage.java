package com.github.linyuzai.connection.loadbalance.core.message;

import java.nio.ByteBuffer;

/**
 * 二进制的 ping 消息
 */
public class BinaryPingMessage extends BinaryMessage implements PingMessage {

    public BinaryPingMessage() {
        this(ByteBuffer.allocate(0));
    }

    public BinaryPingMessage(ByteBuffer payload) {
        super(payload);
    }
}
