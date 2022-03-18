package com.github.linyuzai.plugin.jar.match;

import com.github.linyuzai.plugin.core.util.ReflectionUtils;

import java.lang.annotation.Annotation;
import java.util.List;
import java.util.Map;

public class InstanceListMatcher extends InstanceMatcher {

    private final Class<?> listClass;

    public InstanceListMatcher(Class<?> listClass, Class<?> target, Annotation[] annotations) {
        super(target, annotations);
        this.listClass = listClass;
    }

    @Override
    public Object convert(Map<String, Object> map) {
        List<Object> list = ReflectionUtils.newList(listClass);
        list.addAll(map.values());
        return list;
    }
}
