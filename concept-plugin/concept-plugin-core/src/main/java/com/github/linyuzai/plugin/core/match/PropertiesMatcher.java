package com.github.linyuzai.plugin.core.match;

import com.github.linyuzai.plugin.core.context.PluginContext;
import com.github.linyuzai.plugin.core.filter.PropertiesFilter;
import com.github.linyuzai.plugin.core.handle.HandlerDependency;
import com.github.linyuzai.plugin.core.resolve.PropertiesResolver;
import lombok.Getter;

import java.lang.annotation.Annotation;
import java.util.Properties;
import java.util.function.Supplier;

/**
 * {@link Properties} 匹配器
 */
@Getter
@HandlerDependency(PropertiesResolver.class)
public class PropertiesMatcher extends AbstractPluginMatcher<Supplier<Properties>> {

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
        return Properties.class;
    }

    @Override
    public boolean doFilter(Supplier<Properties> source, PluginContext context) {
        //TODO 需要过滤？
        return true;
    }
}
