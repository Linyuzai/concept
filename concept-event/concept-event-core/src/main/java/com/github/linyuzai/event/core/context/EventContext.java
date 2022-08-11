package com.github.linyuzai.event.core.context;

/**
 * 事件上下文
 * <p>
 * 用于在发布和订阅的过程中传递一些属性
 * <p>
 * 包括事件交换机，事件编码器，事件解码器，事件发布器，事件订阅器等等内部组件
 * <p>
 * 也可以传入一些自定属性进行扩展
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

    /**
     * 是否包含 key
     */
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
