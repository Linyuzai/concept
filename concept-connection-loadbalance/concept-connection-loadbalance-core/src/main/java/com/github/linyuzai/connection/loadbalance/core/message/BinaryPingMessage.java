package com.github.linyuzai.connection.loadbalance.core.message;

import java.nio.ByteBuffer;

/**
 * 二进制的 ping 消息。
 * <p>
 * Ping message has binary payload.
 */
public class BinaryPingMessage extends BinaryMessage implements PingMessage {

    public BinaryPingMessage() {
        super(new byte[0]);
    }

    public BinaryPingMessage(byte[] payload) {
        super(payload);
    }

    public BinaryPingMessage(ByteBuffer payload) {
        super(payload);
    }
}
