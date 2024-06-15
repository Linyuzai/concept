package com.github.linyuzai.plugin.core.extract;

import com.github.linyuzai.plugin.core.context.PluginContext;
import com.github.linyuzai.plugin.core.match.PluginContextMatcher;
import com.github.linyuzai.plugin.core.match.PluginMatcher;
import com.github.linyuzai.plugin.core.util.ReflectionUtils;

import java.lang.annotation.Annotation;
import java.lang.reflect.Type;

/**
 * {@link PluginContext} 提取器
 *
 * @param <T> {@link PluginContext} 类型
 */
public abstract class PluginContextExtractor<T extends PluginContext> extends AbstractPluginExtractor<T> {

    /**
     * 如果插件类型为 {@link PluginContext} 则返回 {@link PluginContextMatcher}，
     * 否则返回 null。
     *
     * @param type        插件类型 {@link Type}
     * @param annotations 注解
     * @return {@link PluginContextMatcher} 或 null
     */
    @Override
    public PluginMatcher getMatcher(Type type, Annotation[] annotations) {
        Class<?> clazz = ReflectionUtils.toClass(type);
        if (clazz == null) {
            return null;
        }
        if (PluginContext.class.isAssignableFrom(clazz)) {
            return new PluginContextMatcher(clazz);
        }
        return null;
    }

    @Override
    public void onExtract(T plugin, PluginContext context) {
        onExtract(plugin);
    }

    public abstract void onExtract(T context);
}
