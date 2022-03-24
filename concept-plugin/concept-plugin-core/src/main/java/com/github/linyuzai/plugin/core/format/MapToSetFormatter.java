package com.github.linyuzai.plugin.core.format;

import com.github.linyuzai.plugin.core.util.ReflectionUtils;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.Map;
import java.util.Set;

@Getter
@AllArgsConstructor
public class MapToSetFormatter extends AbstractPluginFormatter<Map<?, ?>, Set<Object>> {

    private Class<?> setClass;

    public MapToSetFormatter() {
        this(Set.class);
    }

    @Override
    public Set<Object> doFormat(Map<?, ?> source) {
        Set<Object> set = ReflectionUtils.newSet(setClass);
        set.addAll(source.values());
        return set;
    }
}
