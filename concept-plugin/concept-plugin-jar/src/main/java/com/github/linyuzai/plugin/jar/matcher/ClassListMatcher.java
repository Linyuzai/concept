package com.github.linyuzai.plugin.jar.matcher;

import com.github.linyuzai.plugin.core.util.ReflectionUtils;

import java.util.List;
import java.util.Map;

public class ClassListMatcher extends ClassMatcher {

    private final Class<?> listClass;

    public ClassListMatcher(Class<?> target, Class<?> listClass) {
        super(target);
        this.listClass = listClass;
    }

    @Override
    public Object map(Map<String, Object> map) {
        List<Object> list = ReflectionUtils.newList(listClass);
        list.addAll(map.values());
        return list;
    }
}
