package com.github.linyuzai.plugin.core.format;

import com.github.linyuzai.plugin.core.exception.PluginException;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class MapToObjectFormatter extends AbstractPluginFormatter<Map<?, ?>, Object> {

    @Override
    public Object doFormat(Map<?, ?> source) {
        List<Object> list = new ArrayList<>(source.values());
        if (list.size() > 1) {
            throw new PluginException("More than one plugin matched: " + list);
        }
        return list.get(0);
    }
}
