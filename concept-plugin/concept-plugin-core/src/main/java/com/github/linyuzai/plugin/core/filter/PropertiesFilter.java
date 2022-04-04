package com.github.linyuzai.plugin.core.filter;

import com.github.linyuzai.plugin.core.concept.Plugin;
import com.github.linyuzai.plugin.core.util.AntPathMatcher;
import lombok.Getter;

import java.util.*;

/**
 * {@link Properties} 过滤器
 */
@Getter
public class PropertiesFilter extends AbstractPluginFilter<Map<Object, Properties>> {

    /**
     * 属性健模式
     */
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

    /**
     * 遍历所有的 {@link Properties} 并过滤重新重新生成新的 {@link Properties}
     *
     * @param plugins 需要过滤的 {@link Properties}
     * @return 过滤之后的 {@link Properties}
     */
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

    /**
     * 匹配属性健模式
     *
     * @param key 属性健
     * @return 如果匹配则返回 true，否则返回 false
     */
    public boolean matchPropertiesKey(String key) {
        for (String k : keys) {
            if (matcher.match(k, key)) {
                return true;
            }
        }
        return false;
    }
}
