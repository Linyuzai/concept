package com.github.linyuzai.plugin.core.resolver;

import com.github.linyuzai.plugin.core.concept.Plugin;
import com.github.linyuzai.plugin.core.context.PluginContext;
import com.github.linyuzai.plugin.core.resolver.dependence.DependOnResolvers;

import java.util.List;
import java.util.Properties;
import java.util.stream.Collectors;

@DependOnResolvers(PropertiesNamePluginResolver.class)
public abstract class PropertiesPluginResolver extends AbstractPluginResolver {

    @Override
    public void resolve(PluginContext context) {
        List<String> propertiesNames = context.get(Plugin.PROPERTIES_NAMES);
        List<Properties> properties = propertiesNames.stream()
                .map(it -> load(context, it))
                .collect(Collectors.toList());
        context.set(Plugin.PROPERTIES, properties);
    }

    public abstract Properties load(PluginContext context, String propertiesName);

    @Override
    public boolean support(PluginContext context) {
        return context.contains(Plugin.PROPERTIES_NAMES);
    }
}
