package com.github.linyuzai.connection.loadbalance.core.message;

import lombok.NoArgsConstructor;

import java.nio.ByteBuffer;

/**
 * 二进制消息。
 * <p>
 * Message has binary payload.
 */
@NoArgsConstructor
public class BinaryMessage extends AbstractMessage<ByteBuffer> {

    public BinaryMessage(ByteBuffer payload) {
        setPayload(payload);
    }
}
