package com.github.linyuzai.connection.loadbalance.core.message;

import java.util.LinkedHashMap;
import java.util.Map;

public abstract class AbstractMessage implements Message {

    private final Map<String, Object> headers = new LinkedHashMap<>();

    @Override
    public Map<String, Object> headers() {
        return headers;
    }
}
