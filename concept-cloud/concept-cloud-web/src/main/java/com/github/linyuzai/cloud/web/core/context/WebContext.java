package com.github.linyuzai.cloud.web.core.context;

import java.util.Map;

/**
 * 上下文
 */
public interface WebContext {

    /**
     * 是否存在指定的 key
     *
     * @param key 指定的 key
     * @return true 存在，false 不存在
     */
    boolean containsKey(Object key);

    /**
     * 往上下文中放入一对 key-value
     *
     * @param key   指定的 key
     * @param value 放入的值 value
     */
    void put(Object key, Object value);

    /**
     * 通过指定的 key 获得对应的值 value
     *
     * @param key 指定的 key
     * @param <V> 值 value 的类型
     * @return 指定的 key 对应的值 value，如果不存在则返回 null
     */
    <V> V get(Object key);

    /**
     * 通过指定的 key 获得对应的值 value
     * <p>
     * 如果不存在则返回指定的默认值
     *
     * @param key          指定的 key
     * @param defaultValue 如果没有对应的值则返回该值
     * @param <V>          值 value 的类型
     * @return 指定的 key 对应的值 value，如果不存在则返回指定的默认值
     */
    <V> V get(Object key, V defaultValue);

    /**
     * 移除指定的 key 对应的值 value
     *
     * @param key 指定的 key
     */
    void remove(Object key);

    /**
     * 重置/清空上下文
     */
    void reset();

    /**
     * 转成只读 {@link Map}
     *
     * @return {@link Map} 结构的上下文
     */
    Map<Object, Object> toMap();
}
