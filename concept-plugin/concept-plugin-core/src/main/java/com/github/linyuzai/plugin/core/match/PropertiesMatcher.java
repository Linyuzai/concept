package com.github.linyuzai.plugin.core.match;

import com.github.linyuzai.plugin.core.concept.Plugin;
import com.github.linyuzai.plugin.core.resolve.DependOnResolvers;
import com.github.linyuzai.plugin.core.resolve.PropertiesPluginResolver;

import java.lang.annotation.Annotation;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Properties;

@DependOnResolvers(PropertiesPluginResolver.class)
public class PropertiesMatcher extends AbstractPluginMatcher<Map<String, Properties>> {

    public PropertiesMatcher(Annotation[] annotations) {
        super(annotations);
    }

    @Override
    public Object getKey() {
        return Plugin.PROPERTIES;
    }

    @Override
    public Map<String, Properties> filter(Map<String, Properties> propertiesMap) {
        Map<String, Properties> map = new LinkedHashMap<>();
        for (Map.Entry<String, Properties> entry : propertiesMap.entrySet()) {
            if (filterWithAnnotation(entry.getKey())) {
                map.put(entry.getKey(), entry.getValue());
            }
        }
        return map;
    }

    @Override
    public boolean isEmpty(Map<String, Properties> filter) {
        return filter.isEmpty();
    }
}
