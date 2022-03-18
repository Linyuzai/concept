package com.github.linyuzai.plugin.core.extract;

import com.github.linyuzai.plugin.core.concept.Plugin;
import com.github.linyuzai.plugin.core.match.PluginMatcher;
import com.github.linyuzai.plugin.core.match.PluginObjectMatcher;
import com.github.linyuzai.plugin.core.util.ReflectionUtils;

import java.lang.reflect.Type;

public abstract class PluginObjectExtractor<T extends Plugin> extends AbstractPluginExtractor<T> {

    @Override
    public PluginMatcher getMatcher(Type type) {
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
