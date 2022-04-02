package com.github.linyuzai.plugin.core.resolve;

import com.github.linyuzai.plugin.core.concept.Plugin;
import com.github.linyuzai.plugin.core.context.PluginContext;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;

@DependOnResolvers(PropertiesNamePluginResolver.class)
public abstract class PropertiesPluginResolver extends AbstractPluginResolver<List<String>, Map<String, Properties>> {

    @Override
    public Map<String, Properties> doResolve(List<String> propertiesNames, PluginContext context) {
        Map<String, Properties> propertiesMap = new LinkedHashMap<>();
        for (String propertiesName : propertiesNames) {
            propertiesMap.put(propertiesName, load(context, propertiesName));
        }
        return propertiesMap;
    }

    @Override
    public Object getKey() {
        return Plugin.PROPERTIES_NAME;
    }

    @Override
    public Object getResolveKey() {
        return Plugin.PROPERTIES;
    }

    public abstract Properties load(PluginContext context, String propertiesName);
}
