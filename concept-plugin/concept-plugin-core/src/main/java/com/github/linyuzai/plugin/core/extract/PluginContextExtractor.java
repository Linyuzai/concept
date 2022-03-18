package com.github.linyuzai.plugin.core.extract;

import com.github.linyuzai.plugin.core.context.PluginContext;
import com.github.linyuzai.plugin.core.match.PluginContextMatcher;
import com.github.linyuzai.plugin.core.match.PluginMatcher;
import com.github.linyuzai.plugin.core.util.ReflectionUtils;

import java.lang.reflect.Type;

public abstract class PluginContextExtractor<T extends PluginContext> extends AbstractPluginExtractor<T> {

    @Override
    public PluginMatcher getMatcher(Type type) {
        Class<?> clazz = ReflectionUtils.toClass(type);
        if (clazz == null) {
            return null;
        }
        if (PluginContext.class.isAssignableFrom(clazz)) {
            return new PluginContextMatcher(clazz);
        }
        return null;
    }
}
