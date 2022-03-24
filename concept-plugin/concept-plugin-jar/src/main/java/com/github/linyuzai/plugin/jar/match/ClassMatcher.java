package com.github.linyuzai.plugin.jar.match;

import com.github.linyuzai.plugin.core.resolve.DependOnResolvers;
import com.github.linyuzai.plugin.jar.concept.JarPlugin;
import com.github.linyuzai.plugin.jar.resolve.JarClassPluginResolver;

import java.lang.annotation.Annotation;
import java.util.LinkedHashMap;
import java.util.Map;

@DependOnResolvers(JarClassPluginResolver.class)
public class ClassMatcher extends AbstractJarPluginMatcher<Map<String, Class<?>>, Map<String, Class<?>>> {

    public ClassMatcher(Class<?> target, Annotation[] annotations) {
        super(target, annotations);
    }

    @Override
    public Object getKey() {
        return JarPlugin.CLASS;
    }

    public Map<String, Class<?>> filter(Map<String, Class<?>> classes) {
        Map<String, Class<?>> map = new LinkedHashMap<>();
        for (Map.Entry<String, Class<?>> entry : classes.entrySet()) {
            Class<?> value = entry.getValue();
            if (target.isAssignableFrom(value) && filterWithAnnotation(entry.getKey(), value)) {
                map.put(entry.getKey(), value);
            }
        }
        return map;
    }

    @Override
    public boolean isEmpty(Map<String, Class<?>> filter) {
        return filter.isEmpty();
    }
}
