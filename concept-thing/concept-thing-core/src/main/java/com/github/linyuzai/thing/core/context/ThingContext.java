package com.github.linyuzai.thing.core.context;

public interface ThingContext {

    <T> T get(Object key);

    void put(Object key, Object value);

    void remove(Object key);

    void publish(Object event);
}
