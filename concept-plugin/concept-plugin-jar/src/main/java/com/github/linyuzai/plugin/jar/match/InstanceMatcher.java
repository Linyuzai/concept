package com.github.linyuzai.plugin.jar.match;

import com.github.linyuzai.plugin.core.context.PluginContext;
import com.github.linyuzai.plugin.core.match.AbstractPluginMatcher;
import com.github.linyuzai.plugin.core.resolve.DependOnResolvers;
import com.github.linyuzai.plugin.jar.JarPlugin;
import com.github.linyuzai.plugin.jar.resolve.JarInstancePluginResolver;
import lombok.AllArgsConstructor;

import java.lang.annotation.Annotation;
import java.util.LinkedHashMap;
import java.util.Map;

@AllArgsConstructor
@DependOnResolvers(JarInstancePluginResolver.class)
public abstract class InstanceMatcher extends AbstractPluginMatcher {

    protected final Class<?> target;

    protected final Annotation[] annotations;

    @Override
    public Object match(PluginContext context) {
        Map<String, Object> instances = context.get(JarPlugin.INSTANCES);
        Map<String, Object> map = filter(instances);
        if (map.isEmpty()) {
            return null;
        }
        return convert(map);
    }

    public Map<String, Object> filter(Map<String, Object> instances) {
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
