package com.github.linyuzai.plugin.jar.matcher;

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
    public Object map(Map<String, Object> map) {
        Set<Object> set = ReflectionUtils.newSet(setClass);
        set.addAll(map.values());
        return set;
    }
}
