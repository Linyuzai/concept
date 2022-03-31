package com.github.linyuzai.plugin.core.format;

import com.github.linyuzai.plugin.core.exception.PluginException;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * {@link Map} 转单个 {@link Object} 的格式器
 */
public class MapToObjectFormatter extends AbstractPluginFormatter<Map<?, ?>, Object> {

    /**
     * 格式化，获得 {@link Map} 中的 value，如果有多个则抛出 {@link PluginException}
     *
     * @param source 被格式化的对象
     * @return 单个插件对象
     */
    @Override
    public Object doFormat(Map<?, ?> source) {
        List<Object> list = new ArrayList<>(source.values());
        if (list.size() > 1) {
            throw new PluginException("More than one plugin matched: " + list);
        }
        return list.get(0);
    }
}
