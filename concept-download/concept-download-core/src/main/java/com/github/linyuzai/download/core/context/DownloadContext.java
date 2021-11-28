package com.github.linyuzai.download.core.context;

import com.github.linyuzai.download.core.options.DownloadOptions;
import lombok.Getter;

import java.util.HashMap;
import java.util.Map;

/**
 * 下载上下文
 */
public class DownloadContext {

    @Getter
    private final DownloadOptions options;
    /**
     * 保存下载过程中的中间处理数据
     */
    private final Map<Object, Object> map = new HashMap<>();

    public DownloadContext(DownloadOptions options) {
        this.options = options;
    }

    public void set(Object key, Object value) {
        map.put(key, value);
    }

    @SuppressWarnings("unchecked")
    public <T> T get(Object key) {
        return (T) map.get(key);
    }

    public boolean contains(Object key) {
        return map.containsKey(key);
    }
}
