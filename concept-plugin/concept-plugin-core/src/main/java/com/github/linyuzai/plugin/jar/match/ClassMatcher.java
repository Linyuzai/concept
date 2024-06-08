package com.github.linyuzai.plugin.jar.match;

import com.github.linyuzai.plugin.core.context.PluginContext;
import com.github.linyuzai.plugin.core.handle.HandlerDependency;
import com.github.linyuzai.plugin.jar.concept.JarPlugin;
import com.github.linyuzai.plugin.jar.resolve.JarClassResolver;

import java.lang.annotation.Annotation;

/**
 * 类匹配器
 */
@HandlerDependency(JarClassResolver.class)
public class ClassMatcher extends JarPluginMatcher<Class<?>> {

    public ClassMatcher(Class<?> target, Annotation[] annotations) {
        super(target, annotations);
    }

    @Override
    public Object getKey() {
        return JarPlugin.CLASS;
    }

    /**
     * 是对应的类或其子类并基于注解匹配
     *
     * @param clazz 类
     * @return 匹配之后的类
     */
    @Override
    public boolean doFilter(Class<?> clazz, PluginContext context) {
        return target.isAssignableFrom(clazz) && applyFilters(clazz);
    }
}
