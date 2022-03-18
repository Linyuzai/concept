package com.github.linyuzai.plugin.jar.match;

import com.github.linyuzai.plugin.core.util.ReflectionUtils;

import java.util.Map;
import java.util.Set;

public class ClassSetMatcher extends ClassMatcher {

    private final Class<?> setClass;

    public ClassSetMatcher(Class<?> target, Class<?> setClass) {
        super(target);
        this.setClass = setClass;
    }

    @Override
    public Object convert(Map<String, Class<?>> map) {
        Set<Class<?>> set = ReflectionUtils.newSet(setClass);
        set.addAll(map.values());
        return set;
    }
}
