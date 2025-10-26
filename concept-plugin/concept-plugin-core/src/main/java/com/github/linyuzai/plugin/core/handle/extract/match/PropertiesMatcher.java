package com.github.linyuzai.plugin.core.handle.extract.match;

import com.github.linyuzai.plugin.core.context.PluginContext;
import com.github.linyuzai.plugin.core.handle.HandlerDependency;
import com.github.linyuzai.plugin.core.handle.resolve.PropertiesResolver;
import com.github.linyuzai.plugin.core.handle.resolve.PropertiesSupplier;
import lombok.Getter;

import java.lang.annotation.Annotation;
import java.util.Properties;

/**
 * {@link Properties} 匹配器
 */
@Getter
@HandlerDependency(PropertiesResolver.class)
public class PropertiesMatcher extends AbstractPluginMatcher<PropertiesSupplier> {

    public PropertiesMatcher(Annotation[] annotations) {
        super(annotations);
    }

    /**
     * 匹配properties
     */
    @Override
    public Object getKey() {
        return PropertiesSupplier.class;
    }

    @Override
    public boolean doFilter(PropertiesSupplier propertiesSupplier, PluginContext context) {
        return true;
    }
}
