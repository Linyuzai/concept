package com.github.linyuzai.plugin.jar.match;

import com.github.linyuzai.plugin.core.context.PluginContext;
import com.github.linyuzai.plugin.core.match.AbstractPluginMatcher;
import com.github.linyuzai.plugin.core.resolve.DependOnResolvers;
import com.github.linyuzai.plugin.jar.JarPlugin;
import com.github.linyuzai.plugin.jar.resolve.JarClassPluginResolver;
import lombok.AllArgsConstructor;

import java.util.*;

@AllArgsConstructor
@DependOnResolvers(JarClassPluginResolver.class)
public abstract class ClassMatcher extends AbstractPluginMatcher {

    protected final Class<?> target;

    @Override
    public Object match(PluginContext context) {
        Map<String, Class<?>> classes = context.get(JarPlugin.CLASSES);
        Map<String, Class<?>> map = filterByClass(classes, target);
        if (map.isEmpty()) {
            return null;
        }
        return convert(map);
    }

    public Map<String, Class<?>> filterByClass(Map<String, Class<?>> classes, Class<?> target) {
        Map<String, Class<?>> map = new LinkedHashMap<>();
        for (Map.Entry<String, Class<?>> entry : classes.entrySet()) {
            Class<?> value = entry.getValue();
            if (target.isAssignableFrom(value)) {
                map.put(entry.getKey(), value);
            }
        }
        return map;
    }

    public abstract Object convert(Map<String, Class<?>> map);
}
