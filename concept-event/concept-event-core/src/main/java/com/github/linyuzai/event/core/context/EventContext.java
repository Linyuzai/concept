package com.github.linyuzai.event.core.context;

public interface EventContext {

    <V> V get(Object key);

    void put(Object key, Object value);

    void clear();

    EventContext duplicate();
}
