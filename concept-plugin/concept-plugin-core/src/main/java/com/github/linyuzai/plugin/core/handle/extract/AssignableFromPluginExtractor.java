package com.github.linyuzai.plugin.core.handle.extract;

import com.github.linyuzai.plugin.core.handle.extract.format.PluginFormatter;
import com.github.linyuzai.plugin.core.handle.extract.match.PluginMatcher;
import com.github.linyuzai.plugin.core.type.NestedType;

import java.lang.annotation.Annotation;

/**
 * 子类提取器
 */
public abstract class AssignableFromPluginExtractor<T> extends AbstractPluginExtractor<T> {

    @Override
    public PluginMatcher getMatcher(NestedType type, Annotation[] annotations) {
        Class<?> cls = type.toClass();
        if (getSuperClass().isAssignableFrom(cls)) {
            return getMatcher(cls);
        }
        return null;
    }

    /**
     * 获取父类型
     */
    protected abstract Class<?> getSuperClass();

    /**
     * 获得匹配器
     */
    protected abstract PluginMatcher getMatcher(Class<?> cls);

    @Override
    public PluginFormatter getFormatter(NestedType type, Annotation[] annotations) {
        return null;
    }
}
