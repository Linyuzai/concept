package com.github.linyuzai.plugin.core.matcher;

import com.github.linyuzai.plugin.core.concept.Plugin;
import com.github.linyuzai.plugin.core.context.PluginContext;
import com.github.linyuzai.plugin.core.exception.PluginException;
import com.github.linyuzai.plugin.core.resolver.dependence.DependOnResolvers;
import com.github.linyuzai.plugin.core.resolver.PropertiesPluginResolver;

import java.lang.annotation.Annotation;
import java.lang.reflect.Type;
import java.util.List;
import java.util.Properties;

@DependOnResolvers(PropertiesPluginResolver.class)
public abstract class PropertiesMatcher<T> extends GenericTypePluginMatcher<T> {

    @Override
    public boolean tryMatch(PluginContext context, Type type, Annotation[] annotations) {
        if (type instanceof Class && Properties.class.isAssignableFrom((Class<?>) type)) {
            List<Properties> properties = context.get(Plugin.PROPERTIES);
            if (properties.isEmpty()) {
                return false;
            }
            if (properties.size() > 1) {
                throw new PluginException("Multi properties found, try PropertiesListMatcher");
            }
            context.set(this, properties.get(0));
            return true;
        }
        return false;
    }
}
