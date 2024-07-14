package com.github.linyuzai.plugin.core.handle.extract.format;

import com.github.linyuzai.plugin.core.util.ReflectionUtils;
import lombok.*;

import java.util.List;

/**
 * 转 {@link List} 的格式器
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ListFormatter extends TreeValueFormatter<List<Object>> {

    /**
     * {@link List} 的类型
     */
    private Class<?> listClass = List.class;

    @Override
    public List<Object> doFormat(List<Object> objects) {
        List<Object> list = ReflectionUtils.newList(listClass);
        list.addAll(objects);
        return list;
    }
}
