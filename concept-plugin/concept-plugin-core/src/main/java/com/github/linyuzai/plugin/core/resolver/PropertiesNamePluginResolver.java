package com.github.linyuzai.plugin.core.resolver;

import com.github.linyuzai.plugin.core.concept.Plugin;
import com.github.linyuzai.plugin.core.context.PluginContext;
import com.github.linyuzai.plugin.core.resolver.dependence.DependOnResolvers;

import java.util.List;
import java.util.stream.Collectors;

@DependOnResolvers(FileNamePluginResolver.class)
public class PropertiesNamePluginResolver extends AbstractPluginResolver {

    @Override
    public void resolve(PluginContext context) {
        List<String> filenames = context.get(Plugin.FILE_NAMES);
        List<String> propertiesNames = filenames.stream()
                .filter(it -> it.endsWith(".properties"))
                .collect(Collectors.toList());
        context.set(Plugin.PROPERTIES_NAMES, propertiesNames);
    }

    @Override
    public boolean support(PluginContext context) {
        return context.contains(Plugin.FILE_NAMES);
    }
}
