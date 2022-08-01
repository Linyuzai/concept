package com.github.linyuzai.event.core.context;

/**
 * 事件上下文
 */
public interface EventContext {

    /**
     * 根据 key 获得 value
     */
    <V> V get(Object key);

    /**
     * 添加 key-value 到上下文
     */
    void put(Object key, Object value);

    boolean contains(Object key);

    /**
     * 清空上下文
     */
    void clear();

    /**
     * 复制上下文
     */
    EventContext duplicate();
}
