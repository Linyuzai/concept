package com.github.linyuzai.connection.loadbalance.core.message;

import lombok.AllArgsConstructor;

@AllArgsConstructor
public class BinaryMessage extends AbstractMessage {

    private byte[] bytes;

    @Override
    public byte[] getPayload() {
        return bytes;
    }
}
