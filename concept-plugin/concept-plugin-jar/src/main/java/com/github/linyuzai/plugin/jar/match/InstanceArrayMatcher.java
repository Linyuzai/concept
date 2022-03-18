package com.github.linyuzai.plugin.jar.match;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class InstanceArrayMatcher extends InstanceMatcher {

    public InstanceArrayMatcher(Class<?> target) {
        super(target);
    }

    @Override
    public Object convert(Map<String, Object> map) {
        List<Object> values = new ArrayList<>(map.values());
        Object array = Array.newInstance(target, values.size());
        for (int i = 0; i < values.size(); i++) {
            Object o = values.get(i);
            Array.set(array, i, o);
        }
        return array;
    }
}
