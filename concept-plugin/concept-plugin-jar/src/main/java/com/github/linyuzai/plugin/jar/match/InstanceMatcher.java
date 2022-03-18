package com.github.linyuzai.plugin.jar.match;

import com.github.linyuzai.plugin.core.context.PluginContext;
import com.github.linyuzai.plugin.core.match.AbstractPluginMatcher;
import com.github.linyuzai.plugin.core.resolve.DependOnResolvers;
import com.github.linyuzai.plugin.jar.JarPlugin;
import com.github.linyuzai.plugin.jar.resolve.JarInstancePluginResolver;
import lombok.AllArgsConstructor;

import java.util.LinkedHashMap;
import java.util.Map;

@AllArgsConstructor
@DependOnResolvers(JarInstancePluginResolver.class)
public abstract class InstanceMatcher extends AbstractPluginMatcher {

    protected final Class<?> target;

    @Override
    public Object match(PluginContext context) {
        Map<String, Object> instances = context.get(JarPlugin.INSTANCES);
        Map<String, Object> map = filterByClass(instances, target);
        if (map.isEmpty()) {
            return null;
        }
        return convert(map);
    }

    public Map<String, Object> filterByClass(Map<String, Object> instances, Class<?> target) {
        Map<String, Object> map = new LinkedHashMap<>();
        for (Map.Entry<String, Object> entry : instances.entrySet()) {
            Object value = entry.getValue();
            if (target.isInstance(value)) {
                map.put(entry.getKey(), value);
            }
        }
        return map;
    }

    public abstract Object convert(Map<String, Object> map);
}
