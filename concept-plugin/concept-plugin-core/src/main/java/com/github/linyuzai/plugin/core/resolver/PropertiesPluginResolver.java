package com.github.linyuzai.plugin.core.resolver;

import com.github.linyuzai.plugin.core.concept.Plugin;
import com.github.linyuzai.plugin.core.context.PluginContext;
import com.github.linyuzai.plugin.core.resolver.dependence.DependOnResolvers;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;

@DependOnResolvers(PropertiesNamePluginResolver.class)
public abstract class PropertiesPluginResolver extends AbstractPluginResolver {

    @Override
    public void resolve(PluginContext context) {
        List<String> propertiesNames = context.get(Plugin.PROPERTIES_NAMES);
        Map<String, Properties> propertiesMap = new LinkedHashMap<>();
        for (String propertiesName : propertiesNames) {
            propertiesMap.put(propertiesName, load(context, propertiesName));
        }
        context.set(Plugin.PROPERTIES, propertiesMap);
    }

    public abstract Properties load(PluginContext context, String propertiesName);

    @Override
    public boolean support(PluginContext context) {
        return context.contains(Plugin.PROPERTIES_NAMES);
    }
}
