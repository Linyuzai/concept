package com.github.linyuzai.plugin.core.resolve;

import com.github.linyuzai.plugin.core.concept.Plugin;
import com.github.linyuzai.plugin.core.context.PluginContext;

import java.util.List;
import java.util.stream.Collectors;

@DependOnResolvers(PathNamePluginResolver.class)
public class PropertiesNamePluginResolver extends AbstractPluginResolver {

    @Override
    public void resolve(PluginContext context) {
        List<String> filenames = context.get(Plugin.PATH_NAME);
        List<String> propertiesNames = filenames.stream()
                .filter(it -> it.endsWith(".properties"))
                .collect(Collectors.toList());
        context.set(Plugin.PROPERTIES_NAME, propertiesNames);
    }

    @Override
    public boolean support(PluginContext context) {
        return context.contains(Plugin.PATH_NAME);
    }
}
