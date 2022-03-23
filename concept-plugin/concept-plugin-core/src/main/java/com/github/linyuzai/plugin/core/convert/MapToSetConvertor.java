package com.github.linyuzai.plugin.core.convert;

import com.github.linyuzai.plugin.core.util.ReflectionUtils;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.Map;
import java.util.Set;

@Getter
@AllArgsConstructor
public class MapToSetConvertor extends AbstractPluginConvertor<Map<?, ?>, Set<Object>> {

    private Class<?> setClass;

    public MapToSetConvertor() {
        this(Set.class);
    }

    @Override
    public Set<Object> doConvert(Map<?, ?> source) {
        Set<Object> set = ReflectionUtils.newSet(setClass);
        set.addAll(source.values());
        return set;
    }
}
