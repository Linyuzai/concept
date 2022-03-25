package com.github.linyuzai.plugin.core.filter;

import com.github.linyuzai.plugin.core.concept.Plugin;
import com.github.linyuzai.plugin.core.util.AntPathMatcher;

import java.util.*;

public class PropertiesFilter extends AbstractPluginFilter<Map<Object, Properties>> {

    private final Collection<String> keys;

    private final AntPathMatcher matcher = new AntPathMatcher(".");

    public PropertiesFilter(String... keys) {
        this(Arrays.asList(keys));
    }

    public PropertiesFilter(Collection<String> keys) {
        this.keys = keys;
    }

    @Override
    public Object getKey() {
        return Plugin.PROPERTIES;
    }

    @Override
    public Map<Object, Properties> doFilter(Map<Object, Properties> plugins) {
        Map<Object, Properties> map = new LinkedHashMap<>();
        for (Map.Entry<Object, Properties> entry : plugins.entrySet()) {
            Properties properties = entry.getValue();
            Set<String> propertyNames = properties.stringPropertyNames();
            Properties newProperties = new Properties();
            for (String propertyName : propertyNames) {
                if (filterWithNegation(matchPropertiesKey(propertyName))) {
                    String propertyValue = properties.getProperty(propertyName);
                    newProperties.setProperty(propertyName, propertyValue);
                }
            }
            if (!newProperties.isEmpty()) {
                map.put(entry.getKey(), newProperties);
            }
        }
        return map;
    }

    public boolean matchPropertiesKey(String key) {
        for (String k : keys) {
            if (matcher.match(k, key)) {
                return true;
            }
        }
        return false;
    }
}
