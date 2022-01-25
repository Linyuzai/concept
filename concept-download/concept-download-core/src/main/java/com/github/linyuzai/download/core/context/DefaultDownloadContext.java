package com.github.linyuzai.download.core.context;

import com.github.linyuzai.download.core.options.DownloadOptions;

import java.util.HashMap;
import java.util.Map;

/**
 * {@link DownloadContext} 的默认实现。
 * <p>
 * The default implementation of {@link DownloadContext}.
 */
public class DefaultDownloadContext extends AbstractDownloadContext {

    private final Map<Object, Object> map = new HashMap<>();

    public DefaultDownloadContext(String id, DownloadOptions options) {
        super(id, options);
    }

    /**
     * 在内部的 {@link HashMap} 中设置一个键值对。
     * <p>
     * Set a key-value in the internal {@link HashMap}.
     *
     * @param key   键
     *              <p>
     *              Key
     * @param value 值
     *              <p>
     */
    @Override
    public void set(Object key, Object value) {
        map.put(key, value);
    }

    /**
     * 在内部的 {@link HashMap} 中根据键获得一个值。
     * <p>
     * Get a value with the key in the internal {@link HashMap}.
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
    @SuppressWarnings("unchecked")
    @Override
    public <T> T get(Object key) {
        return (T) map.get(key);
    }

    /**
     * 判断健是否在内部的 {@link HashMap} 中存在。
     * <p>
     * Determine whether the key exists in the internal {@link HashMap}.
     *
     * @param key 键
     *            <p>
     *            Key
     * @return 如果存在则返回 true
     * <p>
     * Return true if it exists
     */
    @Override
    public boolean contains(Object key) {
        return map.containsKey(key);
    }

    /**
     * 通过键移除在内部的 {@link HashMap} 中的一个键值对。
     * <p>
     * Remove a key-value by key in the internal {@link HashMap}.
     *
     * @param key 键
     *            <p>
     *            Key
     */
    @Override
    public void remove(Object key) {
        map.remove(key);
    }

    /**
     * 销毁并清空 {@link HashMap}。
     * <p>
     * Destroy and clear {@link HashMap}.
     */
    @Override
    public void destroy() {
        super.destroy();
        map.clear();
    }
}
