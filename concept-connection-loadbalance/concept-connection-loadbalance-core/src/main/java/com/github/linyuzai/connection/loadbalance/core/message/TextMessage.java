package com.github.linyuzai.connection.loadbalance.core.message;

import lombok.AllArgsConstructor;

@AllArgsConstructor
public class TextMessage extends AbstractMessage {

    private String s;

    @SuppressWarnings("unchecked")
    @Override
    public String getPayload() {
        return s;
    }
}
