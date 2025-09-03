package com.github.linyuzai.plugin.core.handle.extract.format;

import com.github.linyuzai.plugin.core.util.ReflectionUtils;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;
import java.util.Set;

/**
 * 转 {@link Set} 的格式化器
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class SetFormatter extends TreeValueFormatter<Set<Object>> {

    /**
     * {@link Set} 的类型
     */
    private Class<?> setClass = Set.class;

    @Override
    public Set<Object> doFormat(List<Object> objects) {
        Set<Object> set = ReflectionUtils.newSet(setClass);
        set.addAll(objects);
        return set;
    }
}
