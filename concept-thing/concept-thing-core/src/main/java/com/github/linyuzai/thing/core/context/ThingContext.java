package com.github.linyuzai.thing.core.context;

import com.github.linyuzai.thing.core.action.ThingActionChain;

public interface ThingContext {

    <T> T get(Object key);

    void put(Object key, Object value);

    void remove(Object key);

    ThingActionChain actions();

    void publish(Object event);
}
