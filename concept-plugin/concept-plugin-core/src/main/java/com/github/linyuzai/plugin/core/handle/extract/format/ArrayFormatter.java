package com.github.linyuzai.plugin.core.handle.extract.format;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.lang.reflect.Array;
import java.util.List;

/**
 * 转数组的格式化器
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ArrayFormatter extends TreeValueFormatter<Object> {

    /**
     * 数组的类型
     */
    private Class<?> arrayClass = Object[].class;

    @Override
    public Object doFormat(List<Object> objects) {
        Object array = Array.newInstance(arrayClass.getComponentType(), objects.size());
        for (int i = 0; i < objects.size(); i++) {
            Array.set(array, i, objects.get(i));
        }
        return array;
    }
}
