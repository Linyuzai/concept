package com.github.linyuzai.plugin.core.format;

import com.github.linyuzai.plugin.core.util.ReflectionUtils;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.List;
import java.util.Map;

/**
 * {@link Map} 转 {@link List} 的格式器
 */
@Getter
@AllArgsConstructor
public class MapToListFormatter extends AbstractPluginFormatter<Map<?, ?>, List<Object>> {

    /**
     * {@link List} 的类型
     */
    private Class<?> listClass;

    public MapToListFormatter() {
        this(List.class);
    }

    /**
     * 格式化，根据 {@link List} 类型实例化并添加 {@link Map} 的 value
     *
     * @param source 被格式化的对象
     * @return {@link List} 格式的插件
     */
    @Override
    public List<Object> doFormat(Map<?, ?> source) {
        List<Object> list = ReflectionUtils.newList(listClass);
        list.addAll(source.values());
        return list;
    }
}
