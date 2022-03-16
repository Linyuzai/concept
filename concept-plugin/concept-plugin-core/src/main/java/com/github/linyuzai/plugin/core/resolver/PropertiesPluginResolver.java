package com.github.linyuzai.plugin.core.resolver;

import com.github.linyuzai.plugin.core.concept.Plugin;
import com.github.linyuzai.plugin.core.context.PluginContext;
import com.github.linyuzai.plugin.core.resolver.dependence.DependOnResolvers;

import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.function.Function;
import java.util.stream.Collectors;

@DependOnResolvers(PropertiesNamePluginResolver.class)
public abstract class PropertiesPluginResolver extends AbstractPluginResolver {

    @Override
    public void resolve(PluginContext context) {
        List<String> propertiesNames = context.get(Plugin.PROPERTIES_NAMES);
        Map<String, Properties> properties = propertiesNames.stream()
                .collect(Collectors.toMap(Function.identity(), it -> load(context, it)));
        context.set(Plugin.PROPERTIES, properties);
    }

    public abstract Properties load(PluginContext context, String propertiesName);

    @Override
    public boolean support(PluginContext context) {
        return context.contains(Plugin.PROPERTIES_NAMES);
    }
}
