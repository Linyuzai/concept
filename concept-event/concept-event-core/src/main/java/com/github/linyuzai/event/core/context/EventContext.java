package com.github.linyuzai.event.core.context;

public interface EventContext {

    <V> V get(Object key);
}
