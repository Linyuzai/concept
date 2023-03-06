package com.github.linyuzai.cloud.web.core.context;

import java.util.Map;

public interface WebContext {

    void put(Object key, Object value);

    <V> V get(Object key);

    <V> V get(Object key, V defaultValue);

    void remove(Object key);

    Map<Object, Object> toMap();

    void reset();

    enum Request {
        PATH
    }

    enum Response {
        BODY
    }
}
