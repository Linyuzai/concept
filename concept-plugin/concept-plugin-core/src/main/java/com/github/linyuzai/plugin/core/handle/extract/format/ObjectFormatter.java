package com.github.linyuzai.plugin.core.handle.extract.format;

import com.github.linyuzai.plugin.core.exception.PluginException;

import java.util.List;

/**
 * 转单个 {@link Object} 的格式器
 */
public class ObjectFormatter extends TreeValueFormatter<Object> {

    @Override
    public Object doFormat(List<Object> objects) {
        if (objects.size() > 1) {
            throw new PluginException("More than one plugin matched: " + objects);
        }
        return objects.get(0);
    }
}
