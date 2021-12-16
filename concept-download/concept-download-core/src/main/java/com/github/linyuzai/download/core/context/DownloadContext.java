package com.github.linyuzai.download.core.context;

import com.github.linyuzai.download.core.options.DownloadOptions;
import lombok.Getter;

import java.util.HashMap;
import java.util.Map;

/**
 * 下载上下文 / Context of download
 * 在下载操作中提供数据的隔离和共享 / Provides data isolation and sharing during download operations
 */
public class DownloadContext {

    @Getter
    private final DownloadOptions options;

    private final Map<Object, Object> map = new HashMap<>();

    /**
     * 上下文依赖一个下载操作的参数 / Context depend on a download options
     *
     * @param options 下载操作参数 / Options of download
     */
    public DownloadContext(DownloadOptions options) {
        this.options = options;
    }

    /**
     * 在上下文中设置一个键值对 / Set a key-value in the context
     *
     * @param key   键 / Key
     * @param value 值 / value
     */
    public void set(Object key, Object value) {
        map.put(key, value);
    }

    /**
     * 在上下文中根据键获得一个值 / Get a value with the key
     * 不存在返回null / Return null if not existed
     *
     * @param key 键 / Key
     * @param <T> 类型 / Type
     * @return 值 / Value
     */
    @SuppressWarnings("unchecked")
    public <T> T get(Object key) {
        return (T) map.get(key);
    }

    /**
     * 判断一个键是否存在 / if a key exists
     *
     * @param key 键 / Key
     * @return 是否存在 / if exists
     */
    public boolean contains(Object key) {
        return map.containsKey(key);
    }

    /**
     * 通过键移除一个键值对 / Remove a key-value by key
     *
     * @param key 键 / Key
     */
    public void remove(Object key) {
        map.remove(key);
    }

    /**
     * 销毁，清空所有内容 / Clear all content
     */
    public void destroy() {
        map.clear();
    }
}
