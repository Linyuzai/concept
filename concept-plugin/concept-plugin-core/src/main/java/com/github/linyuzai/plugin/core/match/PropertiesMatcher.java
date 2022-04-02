package com.github.linyuzai.plugin.core.match;

import com.github.linyuzai.plugin.core.concept.Plugin;
import com.github.linyuzai.plugin.core.filter.PropertiesFilter;
import com.github.linyuzai.plugin.core.resolve.DependOnResolvers;
import com.github.linyuzai.plugin.core.resolve.PropertiesPluginResolver;

import java.lang.annotation.Annotation;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Properties;

/**
 * {@link Properties} 匹配器
 */
@DependOnResolvers(PropertiesPluginResolver.class)
public class PropertiesMatcher extends AbstractPluginMatcher<Map<Object, Properties>> {

    private PropertiesFilter propertiesFilter;

    public PropertiesMatcher(Annotation[] annotations) {
        super(annotations);
        for (Annotation annotation : annotations) {
            if (annotation.annotationType() == PluginProperties.class) {
                String[] propertiesKeys = ((PluginProperties) annotation).value();
                if (propertiesKeys.length > 0) {
                    propertiesFilter = new PropertiesFilter(propertiesKeys);
                }
            }
        }
    }

    @Override
    public Object getKey() {
        return Plugin.PROPERTIES;
    }

    @Override
    public Map<Object, Properties> filter(Map<Object, Properties> propertiesMap) {
        Map<Object, Properties> map = new LinkedHashMap<>();
        for (Map.Entry<Object, Properties> entry : propertiesMap.entrySet()) {
            Object key = entry.getKey();
            if (key instanceof String) {
                if (filterWithAnnotation((String) key)) {
                    map.put(entry.getKey(), entry.getValue());
                }
            } else {
                map.put(entry.getKey(), entry.getValue());
            }
        }
        if (propertiesFilter != null) {
            return propertiesFilter.doFilter(map);
        }
        return map;
    }

    @Override
    public boolean filterWithAnnotation(String pathAndName) {
        return super.filterWithAnnotation(pathAndName);
    }

    @Override
    public boolean isEmpty(Map<Object, Properties> filter) {
        return filter.isEmpty();
    }
}
