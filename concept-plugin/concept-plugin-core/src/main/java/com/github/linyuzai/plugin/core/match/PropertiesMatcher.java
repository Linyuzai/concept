package com.github.linyuzai.plugin.core.match;

import com.github.linyuzai.plugin.core.concept.Plugin;
import com.github.linyuzai.plugin.core.filter.PropertiesFilter;
import com.github.linyuzai.plugin.core.resolve.DependOnResolvers;
import com.github.linyuzai.plugin.core.resolve.PropertiesPluginResolver;
import lombok.Getter;

import java.lang.annotation.Annotation;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Properties;

/**
 * {@link Properties} 匹配器
 */
@Getter
@DependOnResolvers(PropertiesPluginResolver.class)
public class PropertiesMatcher extends AbstractPluginMatcher<Map<Object, Properties>> {

    /**
     * {@link Properties} 过滤器
     */
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

    /**
     * 遍历所有的 {@link Properties}，先根据路径和名称过滤；
     * 如果 {@link PropertiesFilter} 不为 null 则再使用 {@link PropertiesFilter} 过滤。
     *
     * @param propertiesMap {@link Properties} 插件 {@link Map}
     * @return 过滤之后的 {@link Properties}
     */
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
    public boolean isEmpty(Map<Object, Properties> filtered) {
        return filtered.isEmpty();
    }
}
