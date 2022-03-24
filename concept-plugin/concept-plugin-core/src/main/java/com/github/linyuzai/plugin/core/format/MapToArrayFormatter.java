package com.github.linyuzai.plugin.core.format;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Getter
@AllArgsConstructor
public class MapToArrayFormatter extends AbstractPluginFormatter<Map<?, ?>, Object> {

    private Class<?> arrayClass;

    @Override
    public Object doFormat(Map<?, ?> source) {
        List<Object> values = new ArrayList<>(source.values());
        Object array = Array.newInstance(arrayClass, values.size());
        for (int i = 0; i < values.size(); i++) {
            Object o = values.get(i);
            Array.set(array, i, o);
        }
        return array;
    }
}
