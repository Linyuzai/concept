package com.github.linyuzai.plugin.core.filter;

import com.github.linyuzai.plugin.core.util.AntPathMatcher;
import lombok.Getter;

import java.util.*;
import java.util.function.Supplier;

/**
 * {@link Properties} 过滤器
 */
@Getter
public class PropertiesFilter extends AbstractPluginFilter<Supplier<Properties>> {

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
        return Properties.class;
    }

    /**
     * 遍历所有的 {@link Properties} 并过滤重新重新生成新的 {@link Properties}
     *
     * @param supplier 需要过滤的 {@link Properties}
     * @return 过滤之后的 {@link Properties}
     */
    @Override
    public boolean doFilter(Supplier<Properties> supplier) {
        Properties properties = supplier.get();
        Set<String> propertyNames = properties.stringPropertyNames();
        for (String propertyName : propertyNames) {
            if (matchPropertiesKey(propertyName)) {
                return true;
            }
        }
        return false;
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
