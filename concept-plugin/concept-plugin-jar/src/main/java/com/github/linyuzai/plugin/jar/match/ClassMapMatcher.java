package com.github.linyuzai.plugin.jar.match;

import com.github.linyuzai.plugin.core.util.ReflectionUtils;
import java.util.Map;

public class ClassMapMatcher extends ClassMatcher {

    private final Class<?> mapClass;

    public ClassMapMatcher(Class<?> target, Class<?> mapClass) {
        super(target);
        this.mapClass = mapClass;
    }

    @Override
    public Object convert(Map<String, Class<?>> map) {
        Map<String, Class<?>> newMap = ReflectionUtils.newMap(mapClass);
        newMap.putAll(map);
        return newMap;
    }
}
