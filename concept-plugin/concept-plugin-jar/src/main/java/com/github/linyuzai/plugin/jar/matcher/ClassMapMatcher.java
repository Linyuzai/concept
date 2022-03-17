package com.github.linyuzai.plugin.jar.matcher;

import com.github.linyuzai.plugin.core.util.ReflectionUtils;
import java.util.Map;

public class ClassMapMatcher extends ClassMatcher {

    private final Class<?> mapClass;

    public ClassMapMatcher(Class<?> target, Class<?> mapClass) {
        super(target);
        this.mapClass = mapClass;
    }

    @Override
    public Object map(Map<String, Object> map) {
        Map<String, Object> newMap = ReflectionUtils.newMap(mapClass);
        newMap.putAll(map);
        return newMap;
    }
}
