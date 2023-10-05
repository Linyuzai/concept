package com.github.linyuzai.connection.loadbalance.core.message;

import lombok.NoArgsConstructor;

import java.nio.ByteBuffer;

/**
 * 二进制消息。
 * <p>
 * Message has binary payload.
 */
@NoArgsConstructor
public class BinaryMessage extends AbstractMessage<byte[]> {

    public BinaryMessage(byte[] payload) {
        setPayload(payload);
    }

    public BinaryMessage(ByteBuffer payload) {
        setPayload(payload.array());
    }
}
