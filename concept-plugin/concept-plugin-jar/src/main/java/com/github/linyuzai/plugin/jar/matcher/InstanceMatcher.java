package com.github.linyuzai.plugin.jar.matcher;

import com.github.linyuzai.plugin.core.context.PluginContext;
import com.github.linyuzai.plugin.core.exception.PluginException;
import com.github.linyuzai.plugin.core.matcher.GenericTypePluginMatcher;
import com.github.linyuzai.plugin.core.resolver.dependence.DependOnResolvers;
import com.github.linyuzai.plugin.jar.JarPlugin;
import com.github.linyuzai.plugin.jar.resolver.JarInstancePluginResolver;

import java.lang.reflect.Type;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

@DependOnResolvers(JarInstancePluginResolver.class)
public abstract class InstanceMatcher<T> extends GenericTypePluginMatcher<T> {

    @Override
    public boolean tryMatch(PluginContext context, Type type) {

        Collection<?> instances = context.get(JarPlugin.INSTANCES);
        List<?> matchedInstances = instances.stream()
                .filter(((Class<?>) type)::isInstance)
                .collect(Collectors.toList());
        if (matchedInstances.isEmpty()) {
            return false;
        }
        if (matchedInstances.size() > 1) {
            throw new PluginException("Multi instance found, try InstanceListMatcher");
        }
        context.set(this, matchedInstances.get(0));
        return true;
    }

    public Class<T> getMatchingClass() {
        return null;
    }
}
