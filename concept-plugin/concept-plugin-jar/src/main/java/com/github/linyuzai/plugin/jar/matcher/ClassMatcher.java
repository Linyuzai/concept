package com.github.linyuzai.plugin.jar.matcher;

import com.github.linyuzai.plugin.core.context.PluginContext;
import com.github.linyuzai.plugin.core.exception.PluginException;
import com.github.linyuzai.plugin.core.matcher.GenericTypePluginMatcher;
import com.github.linyuzai.plugin.core.resolver.dependence.DependOnResolvers;
import com.github.linyuzai.plugin.jar.JarPlugin;
import com.github.linyuzai.plugin.jar.resolver.JarClassPluginResolver;

import java.lang.reflect.Type;
import java.util.List;
import java.util.stream.Collectors;

@DependOnResolvers(JarClassPluginResolver.class)
public abstract class ClassMatcher<T> extends GenericTypePluginMatcher<Class<? extends T>> {

    @Override
    public boolean tryMatch(PluginContext context, Type type) {
        List<Class<?>> classes = context.get(JarPlugin.CLASSES);
        List<Class<?>> matchedClasses = classes.stream()
                .filter(((Class<?>) type)::isAssignableFrom)
                .collect(Collectors.toList());
        if (matchedClasses.isEmpty()) {
            return false;
        }
        if (matchedClasses.size() > 1) {
            throw new PluginException("Multi class found, try ClassListMatcher");
        }
        context.set(this, matchedClasses.get(0));
        return true;
    }

    @Override
    public Class<Class<? extends T>> getMatchingClass() {
        return null;
    }
}
