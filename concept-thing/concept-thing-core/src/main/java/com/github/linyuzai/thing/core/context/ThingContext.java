package com.github.linyuzai.thing.core.context;

public interface ThingContext {

    <T> T get(Object key);
}
