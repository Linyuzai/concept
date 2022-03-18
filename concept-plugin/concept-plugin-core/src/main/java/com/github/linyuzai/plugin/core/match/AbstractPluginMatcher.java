package com.github.linyuzai.plugin.core.match;

import com.github.linyuzai.plugin.core.context.PluginContext;
import com.github.linyuzai.plugin.core.exception.PluginException;
import com.github.linyuzai.plugin.core.util.ReflectionUtils;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;

public abstract class AbstractPluginMatcher<T> implements PluginMatcher {

    @Override
    public Object match(PluginContext context) {
        Map<String, T> bytesMap = context.get(getKey());
        Map<String, Object> map = filter(bytesMap);
        if (map.isEmpty()) {
            return null;
        }
        return convert(map);
    }

    public abstract Object getKey();

    public abstract Map<String, Object> filter(Map<String, T> map);

    public Object convert(Map<String, Object> map) {
        if (this instanceof MapConvertor) {
            Map<String, Object> newMap = ReflectionUtils.newMap(((MapConvertor) this).getMapClass());
            newMap.putAll(map);
            return newMap;
        }
        if (this instanceof ListConvertor) {
            List<Object> list = ReflectionUtils.newList(((ListConvertor) this).getListClass());
            list.addAll(map.values());
            return list;
        }
        if (this instanceof SetConvertor) {
            Set<Object> set = ReflectionUtils.newSet(((SetConvertor) this).getSetClass());
            set.addAll(map.values());
            return set;
        }
        if (this instanceof ArrayConvertor) {
            List<Object> values = new ArrayList<>(map.values());
            Object array = Array.newInstance(((ArrayConvertor) this).getArrayClass(), values.size());
            for (int i = 0; i < values.size(); i++) {
                Object o = values.get(i);
                Array.set(array, i, o);
            }
            return array;
        }
        if (this instanceof ObjectConvertor) {
            List<Object> list = new ArrayList<>(map.values());
            if (map.size() > 1) {
                throw new PluginException("More than one "
                        + ((ObjectConvertor) this).getType()
                        + " matched: "
                        + list);
            }
            return list.get(0);
        }
        throw new PluginException("Can not convert");
    }
}
