package com.github.linyuzai.plugin.core.convert;

import com.github.linyuzai.plugin.core.match.PluginMatcher;
import com.github.linyuzai.plugin.core.util.ReflectionUtils;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.Map;

@Getter
@AllArgsConstructor
public class MapToMapConvertor extends AbstractPluginConvertor<Map<?, ?>, Map<Object, Object>> {

    private Class<?> mapClass;

    public MapToMapConvertor() {
        this(Map.class);
    }

    @Override
    public Map<Object, Object> doConvert(Map<?, ?> source) {
        Map<Object, Object> newMap = ReflectionUtils.newMap(mapClass);
        newMap.putAll(source);
        return newMap;
    }
}
