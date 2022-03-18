package com.github.linyuzai.plugin.jar.matcher;

import com.github.linyuzai.plugin.core.context.PluginContext;
import com.github.linyuzai.plugin.core.match.AbstractPluginMatcher;
import com.github.linyuzai.plugin.core.resolve.DependOnResolvers;
import com.github.linyuzai.plugin.jar.JarPlugin;
import com.github.linyuzai.plugin.jar.resolver.JarClassPluginResolver;
import lombok.AllArgsConstructor;
import java.util.*;

@AllArgsConstructor
@DependOnResolvers(JarClassPluginResolver.class)
public abstract class ClassMatcher extends AbstractPluginMatcher {

    protected final Class<?> target;

    @Override
    public Object match(PluginContext context) {
        Map<String, Class<?>> classes = context.get(JarPlugin.CLASSES);
        Map<String, Object> map = filterByClass(classes, target);
        if (map.isEmpty()) {
            return null;
        }
        return map(map);
    }

    public Map<String, Object> filterByClass(Map<String, Class<?>> classes, Class<?> target) {
        Map<String, Object> map = new LinkedHashMap<>();
        for (Map.Entry<String, Class<?>> entry : classes.entrySet()) {
            Class<?> value = entry.getValue();
            if (target.isAssignableFrom(value)) {
                map.put(entry.getKey(), value);
            }
        }
        return map;
    }

    public abstract Object map(Map<String, Object> map);
}
