package com.github.linyuzai.plugin.core.handle.extract;

import com.github.linyuzai.plugin.core.context.PluginContext;
import com.github.linyuzai.plugin.core.handle.extract.match.PluginContextMatcher;
import com.github.linyuzai.plugin.core.handle.extract.match.PluginMatcher;
import com.github.linyuzai.plugin.core.type.NestedType;

import java.lang.annotation.Annotation;

/**
 * 插件上下文提取器
 */
public abstract class PluginContextExtractor<T extends PluginContext> extends AssignableFromPluginExtractor<T> {

    @Override
    public PluginMatcher getMatcher(NestedType type, Annotation[] annotations) {
        Class<?> cls = type.toClass();
        if (PluginContext.class.isAssignableFrom(cls)) {
            return new PluginContextMatcher(cls);
        }
        return null;
    }

    @Override
    protected Class<?> getSuperClass() {
        return PluginContext.class;
    }

    @Override
    protected PluginMatcher getMatcher(Class<?> cls) {
        return new PluginContextMatcher(cls);
    }

    @Override
    public void onExtract(T plugin, PluginContext context) {
        onExtract(plugin);
    }

    /**
     * 提取
     */
    public abstract void onExtract(T context);

    /**
     * 插件上下文提取执行器工厂
     */
    public static class InvokerFactory extends AbstractPluginExtractor.InvokerFactory {

        @Override
        protected AbstractPluginExtractor<?> createExtractor() {
            return new PluginContextExtractor<PluginContext>() {
                @Override
                public void onExtract(PluginContext context) {
                }
            };
        }
    }
}
