package com.github.linyuzai.download.core.context;

import com.github.linyuzai.download.core.options.DownloadOptions;

/**
 * 下载上下文。
 * <p>
 * Context of download.
 */
public interface DownloadContext {

    /**
     * 获得唯一ID。
     * <p>
     * Get unique ID
     *
     * @return 唯一ID
     * <p>
     * Unique ID
     */
    String getId();

    /**
     * 获得 {@link DownloadOptions}。
     * <p>
     * Get {@link DownloadOptions}.
     *
     * @return {@link DownloadOptions}
     */
    DownloadOptions getOptions();

    /**
     * 在上下文中设置一个键值对。
     * <p>
     * Set a key-value in the context.
     *
     * @param key   键
     *              <p>
     *              Key
     * @param value 值
     *              <p>
     *              Value
     */
    void set(Object key, Object value);

    /**
     * 在上下文中根据键获得一个值，
     * 不存在返回 null。
     * <p>
     * Get a value with the key.
     * Return null if not existed.
     *
     * @param key 键
     *            <p>
     *            Key
     * @param <T> 类型
     *            <p>
     *            Type
     * @return 值
     * <p>
     * Value
     */
    <T> T get(Object key);

    /**
     * 判断键是否存在。
     * <p>
     * Determine whether the key exists.
     *
     * @param key 键
     *            <p>
     *            Key
     * @return 如果存在则返回 true
     * <p>
     * Return true if it exists
     */
    boolean contains(Object key);

    /**
     * 通过键移除一个键值对。
     * <p>
     * Remove a key-value by key.
     *
     * @param key 键
     *            <p>
     *            Key
     */
    void remove(Object key);

    /**
     * 初始化上下文。
     * <p>
     * Initialize context.
     */
    void initialize();

    /**
     * 销毁上下文。
     * <p>
     * Destroy context.
     */
    void destroy();
}
