package com.github.linyuzai.plugin.core.handle.extract;

import com.github.linyuzai.plugin.core.concept.Plugin;
import com.github.linyuzai.plugin.core.handle.extract.match.PluginMatcher;
import com.github.linyuzai.plugin.core.handle.extract.match.PluginObjectMatcher;
import com.github.linyuzai.plugin.core.util.ReflectionUtils;

import java.lang.annotation.Annotation;
import java.lang.reflect.Type;

/**
 * {@link Plugin} 提取器
 *
 * @param <T> {@link Plugin} 类型
 */
public abstract class PluginObjectExtractor<T extends Plugin> extends AbstractPluginExtractor<T> {

    /**
     * 如果插件类型为 {@link Plugin} 则返回 {@link PluginObjectMatcher}，
     * 否则返回 null。
     *
     * @param type        插件类型 {@link Type}
     * @param annotations 注解
     * @return {@link PluginObjectMatcher} 或 null
     */
    @Override
    public PluginMatcher getMatcher(Type type, Annotation[] annotations) {
        Class<?> clazz = ReflectionUtils.toClass(type);
        if (clazz == null) {
            return null;
        }
        if (Plugin.class.isAssignableFrom(clazz)) {
            return new PluginObjectMatcher(clazz);
        }
        return null;
    }
}
