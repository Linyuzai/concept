package com.github.linyuzai.plugin.jar.matcher;

import com.github.linyuzai.plugin.core.context.PluginContext;
import com.github.linyuzai.plugin.core.matcher.AbstractPluginMatcher;
import com.github.linyuzai.plugin.core.resolver.dependence.DependOnResolvers;
import com.github.linyuzai.plugin.jar.JarPlugin;
import com.github.linyuzai.plugin.core.resolver.PropertiesPluginResolver;

import java.util.List;
import java.util.Properties;

@Deprecated
@DependOnResolvers(PropertiesPluginResolver.class)
public abstract class PropertiesListMatcher extends AbstractPluginMatcher<List<? extends Properties>> {

    @Override
    public boolean tryMatch(PluginContext context) {
        List<Properties> properties = context.get(JarPlugin.PROPERTIES);
        if (properties.isEmpty()) {
            return false;
        }
        context.set(this, properties);
        return true;
    }

    @Override
    public abstract void onMatched(List<? extends Properties> plugins);
}
