package com.github.linyuzai.plugin.core.format;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * {@link Map} 转数组的格式器
 */
@Getter
@AllArgsConstructor
public class MapToArrayFormatter extends AbstractPluginFormatter<Map<?, ?>, Object> {

    /**
     * 数组的类型
     */
    private Class<?> arrayClass;

    /**
     * 格式化，根据数组类型实例化并添加 {@link Map} 的 value
     *
     * @param source 被格式化的对象
     * @return 数组格式的插件
     */
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
