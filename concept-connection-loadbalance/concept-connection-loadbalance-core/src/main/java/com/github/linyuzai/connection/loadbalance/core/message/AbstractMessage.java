package com.github.linyuzai.connection.loadbalance.core.message;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.LinkedHashMap;
import java.util.Map;

@Getter
@AllArgsConstructor
public abstract class AbstractMessage<T> implements Message {

    private final Map<String, String> headers = new LinkedHashMap<>();

    private final T payload;

    @SuppressWarnings("unchecked")
    public T getPayload() {
        return payload;
    }
}
