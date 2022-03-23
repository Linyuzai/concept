package com.github.linyuzai.plugin.core.convert;

import com.github.linyuzai.plugin.core.match.PluginMatcher;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Getter
@AllArgsConstructor
public class MapToArrayConvertor extends AbstractPluginConvertor<Map<?, ?>, Object> {

    private Class<?> arrayClass;

    @Override
    public Object doConvert(Map<?, ?> source) {
        List<Object> values = new ArrayList<>(source.values());
        Object array = Array.newInstance(arrayClass, values.size());
        for (int i = 0; i < values.size(); i++) {
            Object o = values.get(i);
            Array.set(array, i, o);
        }
        return array;
    }
}
