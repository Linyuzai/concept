package com.github.linyuzai.plugin.jar.match;

import com.github.linyuzai.plugin.core.handle.HandlerDependency;
import com.github.linyuzai.plugin.jar.concept.JarPlugin;
import com.github.linyuzai.plugin.jar.resolve.JarClassResolver;

import java.lang.annotation.Annotation;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 * 类匹配器
 */
@HandlerDependency(JarClassResolver.class)
public class ClassMatcher extends JarPluginMatcher<Map<String, Class<?>>> {

    public ClassMatcher(Class<?> target, Annotation[] annotations) {
        super(target, annotations);
    }

    @Override
    public Object getKey() {
        return JarPlugin.CLASS;
    }

    /**
     * 是对应的类或其子类并基于注解匹配
     *
     * @param classes 类
     * @return 匹配之后的类
     */
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
    public boolean isEmpty(Map<String, Class<?>> filtered) {
        return filtered.isEmpty();
    }
}
