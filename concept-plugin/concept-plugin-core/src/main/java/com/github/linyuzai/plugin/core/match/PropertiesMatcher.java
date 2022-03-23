package com.github.linyuzai.plugin.core.match;

import com.github.linyuzai.plugin.core.concept.Plugin;
import com.github.linyuzai.plugin.core.convert.PluginConvertor;
import com.github.linyuzai.plugin.core.resolve.DependOnResolvers;
import com.github.linyuzai.plugin.core.resolve.PropertiesPluginResolver;
import com.github.linyuzai.plugin.core.util.ReflectionUtils;

import java.lang.annotation.Annotation;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Properties;
import java.util.function.Function;

@DependOnResolvers(PropertiesPluginResolver.class)
public class PropertiesMatcher extends AbstractPluginMatcher<Map<String, Properties>, Map<String, Object>> {

    protected Class<?> target;

    protected Function<Properties, Object> function;

    public PropertiesMatcher(Class<?> target, Annotation[] annotations, PluginConvertor convertor) {
        super(annotations, convertor);
        this.target = target;
        if (target != Properties.class && Map.class.isAssignableFrom(target)) {
            this.function = properties -> {
                Map<String, String> newMap = ReflectionUtils.newMap(target);
                for (String propertyName : properties.stringPropertyNames()) {
                    newMap.put(propertyName, properties.getProperty(propertyName));
                }
                return newMap;
            };
        }
    }

    @Override
    public Object getKey() {
        return Plugin.PROPERTIES;
    }

    @Override
    public Map<String, Object> filter(Map<String, Properties> propertiesMap) {
        Map<String, Object> map = new LinkedHashMap<>();
        for (Map.Entry<String, Properties> entry : propertiesMap.entrySet()) {
            if (function == null) {
                map.put(entry.getKey(), entry.getValue());
            } else {
                map.put(entry.getKey(), function.apply(entry.getValue()));
            }
        }
        return map;
    }

    @Override
    public boolean isEmpty(Map<String, Object> filter) {
        return filter.isEmpty();
    }
}
