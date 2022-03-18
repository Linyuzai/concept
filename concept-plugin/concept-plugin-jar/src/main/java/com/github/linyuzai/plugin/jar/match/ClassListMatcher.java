package com.github.linyuzai.plugin.jar.match;

import com.github.linyuzai.plugin.core.util.ReflectionUtils;

import java.lang.annotation.Annotation;
import java.util.List;
import java.util.Map;

public class ClassListMatcher extends ClassMatcher {

    private final Class<?> listClass;

    public ClassListMatcher(Class<?> listClass, Class<?> target, Annotation[] annotations) {
        super(target, annotations);
        this.listClass = listClass;
    }

    @Override
    public Object convert(Map<String, Class<?>> map) {
        List<Class<?>> list = ReflectionUtils.newList(listClass);
        list.addAll(map.values());
        return list;
    }
}
