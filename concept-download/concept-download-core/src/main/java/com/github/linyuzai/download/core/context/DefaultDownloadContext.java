package com.github.linyuzai.download.core.context;

import com.github.linyuzai.download.core.options.DownloadOptions;

import java.util.HashMap;
import java.util.Map;

/**
 * {@link DownloadContext} 的默认实现。
 */
public class DefaultDownloadContext extends AbstractDownloadContext {

    /**
     * 用来存储上下文数据
     */
    private final Map<Object, Object> map = new HashMap<>();

    public DefaultDownloadContext(String id, DownloadOptions options) {
        super(id, options);
    }

    /**
     * 在内部的 {@link HashMap} 中设置一个键值对。
     *
     * @param key   键
     * @param value 值
     */
    @Override
    public void set(Object key, Object value) {
        map.put(key, value);
    }

    /**
     * 在内部的 {@link HashMap} 中根据键获得一个值。
     *
     * @param key 键
     * @param <T> 类型
     * @return 值
     */
    @SuppressWarnings("unchecked")
    @Override
    public <T> T get(Object key) {
        return (T) map.get(key);
    }

    /**
     * 判断健是否在内部的 {@link HashMap} 中存在。
     *
     * @param key 键
     * @return 如果存在则返回 true
     */
    @Override
    public boolean contains(Object key) {
        return map.containsKey(key);
    }

    /**
     * 通过键移除在内部的 {@link HashMap} 中的一个键值对。
     *
     * @param key 键
     */
    @Override
    public void remove(Object key) {
        map.remove(key);
    }

    /**
     * 销毁并清空 {@link HashMap}。
     */
    @Override
    public void destroy() {
        super.destroy();
        map.clear();
    }
}
