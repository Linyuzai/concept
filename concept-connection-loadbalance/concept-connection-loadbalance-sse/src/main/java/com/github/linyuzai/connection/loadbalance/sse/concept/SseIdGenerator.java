package com.github.linyuzai.connection.loadbalance.sse.concept;

import java.util.Map;

public interface SseIdGenerator {

    Object generateId(Map<Object, Object> metadata);
}
