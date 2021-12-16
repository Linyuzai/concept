package com.github.linyuzai.download.core.context;

import com.github.linyuzai.download.core.options.DownloadOptions;

/**
 * 下载上下文 / Context of download
 * 在下载操作中提供数据的隔离和共享 / Provides data isolation and sharing during download operations
 */
public interface DownloadContext {

    DownloadOptions getOptions();

    /**
     * 在上下文中设置一个键值对 / Set a key-value in the context
     *
     * @param key   键 / Key
     * @param value 值 / value
     */
    void set(Object key, Object value);

    /**
     * 在上下文中根据键获得一个值 / Get a value with the key
     * 不存在返回null / Return null if not existed
     *
     * @param key 键 / Key
     * @param <T> 类型 / Type
     * @return 值 / Value
     */
    <T> T get(Object key);

    /**
     * 判断一个键是否存在 / if a key exists
     *
     * @param key 键 / Key
     * @return 是否存在 / if exists
     */
    boolean contains(Object key);

    /**
     * 通过键移除一个键值对 / Remove a key-value by key
     *
     * @param key 键 / Key
     */
    void remove(Object key);

    /**
     * 销毁，清空所有内容 / Clear all content
     */
    void destroy();
}
