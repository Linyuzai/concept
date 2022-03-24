package com.github.linyuzai.plugin.core.format;

import com.github.linyuzai.plugin.core.util.ReflectionUtils;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.Map;

@Getter
@AllArgsConstructor
public class MapToMapFormatter extends AbstractPluginFormatter<Map<?, ?>, Map<Object, Object>> {

    private Class<?> mapClass;

    public MapToMapFormatter() {
        this(Map.class);
    }

    @Override
    public Map<Object, Object> doFormat(Map<?, ?> source) {
        Map<Object, Object> newMap = ReflectionUtils.newMap(mapClass);
        newMap.putAll(source);
        return newMap;
    }
}
