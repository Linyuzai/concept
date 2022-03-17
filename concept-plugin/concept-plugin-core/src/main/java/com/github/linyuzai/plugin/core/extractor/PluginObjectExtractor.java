package com.github.linyuzai.plugin.core.extractor;

import com.github.linyuzai.plugin.core.concept.Plugin;
import com.github.linyuzai.plugin.core.matcher.PluginMatcher;
import com.github.linyuzai.plugin.core.matcher.PluginObjectMatcher;
import com.github.linyuzai.plugin.core.util.ReflectionUtils;

import java.lang.reflect.Type;

public abstract class PluginObjectExtractor<T extends Plugin> extends AbstractPluginExtractor<T> {

    @Override
    public PluginMatcher bind(Type type) {
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
