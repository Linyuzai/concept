package com.github.linyuzai.connection.loadbalance.core.message;

import lombok.AllArgsConstructor;

@AllArgsConstructor
public class StringMessage extends AbstractMessage{

    private String content;

    @SuppressWarnings("unchecked")
    @Override
    public String getPayload() {
        return content;
    }
}
