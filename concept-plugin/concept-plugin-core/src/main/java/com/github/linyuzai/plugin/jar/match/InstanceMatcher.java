package com.github.linyuzai.plugin.jar.match;

import com.github.linyuzai.plugin.core.context.PluginContext;
import com.github.linyuzai.plugin.core.handle.HandlerDependency;
import com.github.linyuzai.plugin.jar.concept.JarPlugin;
import com.github.linyuzai.plugin.jar.resolve.JarInstanceResolver;

import java.lang.annotation.Annotation;

/**
 * 实例匹配器
 */
@HandlerDependency(JarInstanceResolver.class)
public class InstanceMatcher extends JarPluginMatcher<Object> {

    public InstanceMatcher(Class<?> target, Annotation[] annotations) {
        super(target, annotations);
    }

    @Override
    public Object getKey() {
        return JarPlugin.INSTANCE;
    }

    @Override
    public boolean doFilter(Object instance, PluginContext context) {
        return target.isInstance(instance) && applyFilters(instance.getClass());
    }
}
