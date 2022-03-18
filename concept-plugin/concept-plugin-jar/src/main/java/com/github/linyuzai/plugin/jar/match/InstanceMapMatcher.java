package com.github.linyuzai.plugin.jar.match;

import com.github.linyuzai.plugin.core.util.ReflectionUtils;

import java.util.Map;

public class InstanceMapMatcher extends InstanceMatcher {

    private final Class<?> mapClass;

    public InstanceMapMatcher(Class<?> target, Class<?> mapClass) {
        super(target);
        this.mapClass = mapClass;
    }

    @Override
    public Object convert(Map<String, Object> map) {
        Map<String, Object> newMap = ReflectionUtils.newMap(mapClass);
        newMap.putAll(map);
        return newMap;
    }
}
