package com.github.linyuzai.plugin.jar.matcher;

import com.github.linyuzai.plugin.core.context.PluginContext;
import com.github.linyuzai.plugin.core.exception.PluginException;
import com.github.linyuzai.plugin.core.matcher.AbstractPluginMatcher;
import com.github.linyuzai.plugin.core.resolver.dependence.DependOnResolvers;
import com.github.linyuzai.plugin.jar.JarPlugin;
import com.github.linyuzai.plugin.jar.resolver.JarPropertiesPluginResolver;

import java.util.List;
import java.util.Properties;

@DependOnResolvers(JarPropertiesPluginResolver.class)
public abstract class PropertiesMatcher extends AbstractPluginMatcher<Properties> {

    @Override
    public boolean ifMatch(PluginContext context) {
        List<Properties> properties = context.get(JarPlugin.PROPERTIES);
        if (properties.isEmpty()) {
            return false;
        }
        if (properties.size() > 1) {
            throw new PluginException("Multi properties found, try PropertiesListMatcher");
        }
        context.set(this, properties.get(0));
        return true;
    }
}
