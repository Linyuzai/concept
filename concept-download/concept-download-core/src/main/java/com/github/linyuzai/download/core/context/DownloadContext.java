package com.github.linyuzai.download.core.context;

/**
 * 下载上下文。
 *
 * @see DefaultDownloadContext
 */
public interface DownloadContext {

    /**
     * 在上下文中设置一个键值对。
     *
     * @param key   键
     * @param value 值
     */
    void set(Object key, Object value);

    /**
     * 在上下文中根据键获得一个值，
     * 不存在返回 null。
     *
     * @param key 键
     * @param <T> 类型
     * @return 值
     */
    <T> T get(Object key);

    /**
     * 判断键是否存在。
     *
     * @param key 键
     * @return 如果存在则返回 true
     */
    boolean contains(Object key);

    /**
     * 通过键移除一个键值对。
     *
     * @param key 键
     */
    void remove(Object key);
}
