package com.github.linyuzai.plugin.jar.matcher;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class ClassArrayMatcher extends ClassMatcher {

    public ClassArrayMatcher(Class<?> target) {
        super(target);
    }

    @Override
    public Object map(Map<String, Object> map) {
        List<Object> values = new ArrayList<>(map.values());
        Object array = Array.newInstance(target, values.size());
        for (int i = 0; i < values.size(); i++) {
            Object o = values.get(i);
            Array.set(array, i, o);
        }
        return array;
    }
}
