package com.github.linyuzai.connection.loadbalance.sse.concept;

import java.util.Map;
import java.util.UUID;

public class DefaultSseIdGenerator implements SseIdGenerator {

    @Override
    public Object generateId(Map<Object, Object> metadata) {
        return UUID.randomUUID();
    }
}
