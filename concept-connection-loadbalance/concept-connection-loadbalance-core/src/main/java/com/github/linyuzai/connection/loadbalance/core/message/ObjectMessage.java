package com.github.linyuzai.connection.loadbalance.core.message;

import lombok.AllArgsConstructor;

@AllArgsConstructor
public class ObjectMessage extends AbstractMessage {

    private Object o;

    @SuppressWarnings("unchecked")
    @Override
    public <T> T getPayload() {
        return (T) o;
    }
}
