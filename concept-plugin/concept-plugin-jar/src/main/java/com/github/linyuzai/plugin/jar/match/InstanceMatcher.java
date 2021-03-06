package com.github.linyuzai.plugin.jar.match;

import com.github.linyuzai.plugin.core.resolve.DependOnResolvers;
import com.github.linyuzai.plugin.jar.concept.JarPlugin;
import com.github.linyuzai.plugin.jar.resolve.JarInstanceResolver;

import java.lang.annotation.Annotation;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 * 实例匹配器
 */
@DependOnResolvers(JarInstanceResolver.class)
public class InstanceMatcher extends JarPluginMatcher<Map<String, Object>> {

    public InstanceMatcher(Class<?> target, Annotation[] annotations) {
        super(target, annotations);
    }

    @Override
    public Object getKey() {
        return JarPlugin.INSTANCE;
    }

    /**
     * 是对应类的实例并基于注解匹配
     *
     * @param instances 实例
     * @return 匹配之后的实例
     */
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
    public boolean isEmpty(Map<String, Object> filtered) {
        return filtered.isEmpty();
    }
}
