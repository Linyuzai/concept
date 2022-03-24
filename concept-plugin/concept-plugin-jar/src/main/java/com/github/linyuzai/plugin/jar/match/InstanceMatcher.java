package com.github.linyuzai.plugin.jar.match;

import com.github.linyuzai.plugin.core.convert.PluginConvertor;
import com.github.linyuzai.plugin.core.resolve.DependOnResolvers;
import com.github.linyuzai.plugin.jar.concept.JarPlugin;
import com.github.linyuzai.plugin.jar.resolve.JarInstancePluginResolver;

import java.lang.annotation.Annotation;
import java.util.LinkedHashMap;
import java.util.Map;

@DependOnResolvers(JarInstancePluginResolver.class)
public class InstanceMatcher extends AbstractJarPluginMatcher<Map<String, Object>, Map<String, Object>> {

    public InstanceMatcher(Class<?> target, Annotation[] annotations) {
        super(target, annotations);
    }

    @Override
    public Object getKey() {
        return JarPlugin.INSTANCE;
    }

    public Map<String, Object> filter(Map<String, Object> instances) {
        Map<String, Object> map = new LinkedHashMap<>();
        for (Map.Entry<String, Object> entry : instances.entrySet()) {
            Object value = entry.getValue();
            if (target.isInstance(value) && filterWithAnnotation(entry.getKey(), value.getClass())) {
                map.put(entry.getKey(), value);
            }
        }
        return map;
    }

    @Override
    public boolean isEmpty(Map<String, Object> filter) {
        return filter.isEmpty();
    }
}
