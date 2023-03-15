package com.github.linyuzai.cloud.web.core.context;

import java.util.Map;

public interface WebContext {

    boolean containsKey(Object key);

    void put(Object key, Object value);

    <V> V get(Object key);

    <V> V get(Object key, V defaultValue);

    void remove(Object key);

    Map<Object, Object> toMap();

    void reset();

    interface Request {

        String METHOD = "_request@method";

        String PATH = "_request@path";
    }

    interface Response {
        String BODY = "_response@body";
    }
}
