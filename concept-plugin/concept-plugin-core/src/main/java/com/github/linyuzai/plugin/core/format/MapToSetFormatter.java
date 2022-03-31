package com.github.linyuzai.plugin.core.format;

import com.github.linyuzai.plugin.core.util.ReflectionUtils;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.Map;
import java.util.Set;

/**
 * {@link Map} 转 {@link Set} 的格式器
 */
@Getter
@AllArgsConstructor
public class MapToSetFormatter extends AbstractPluginFormatter<Map<?, ?>, Set<Object>> {

    /**
     * {@link Set} 的类型
     */
    private Class<?> setClass;

    public MapToSetFormatter() {
        this(Set.class);
    }

    /**
     * 格式化，根据 {@link Set} 类型实例化并添加 {@link Map} 的 value
     *
     * @param source 被格式化的对象
     * @return {@link Set} 格式的插件
     */
    @Override
    public Set<Object> doFormat(Map<?, ?> source) {
        Set<Object> set = ReflectionUtils.newSet(setClass);
        set.addAll(source.values());
        return set;
    }
}
