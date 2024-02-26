package com.github.linyuzai.connection.loadbalance.sse.concept;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.util.Map;

@Getter
@RequiredArgsConstructor
public class ParamSseIdGenerator implements SseIdGenerator {

    private final String name;

    @Override
    public Object generateId(Map<Object, Object> metadata) {
        Object id = metadata.get(name);
        if (id == null) {
            throw new IllegalArgumentException("No value found for param name: " + name);
        }
        return id;
    }
}
