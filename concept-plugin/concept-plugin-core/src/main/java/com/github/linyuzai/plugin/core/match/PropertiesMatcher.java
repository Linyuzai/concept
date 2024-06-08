package com.github.linyuzai.plugin.core.match;

import com.github.linyuzai.plugin.core.context.PluginContext;
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

    public PropertiesMatcher(Annotation[] annotations) {
        super(annotations);
    }

    @Override
    public Object getKey() {
        return Properties.class;
    }

    @Override
    public boolean doFilter(Supplier<Properties> source, PluginContext context) {
        return true;
    }
}
