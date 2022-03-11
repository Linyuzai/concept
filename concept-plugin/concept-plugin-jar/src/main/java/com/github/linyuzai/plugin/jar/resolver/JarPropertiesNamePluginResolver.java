package com.github.linyuzai.plugin.jar.resolver;

import com.github.linyuzai.plugin.core.context.PluginContext;
import com.github.linyuzai.plugin.core.resolver.AbstractPluginResolver;
import com.github.linyuzai.plugin.core.resolver.dependence.DependOnResolvers;
import com.github.linyuzai.plugin.jar.JarPlugin;

import java.util.List;
import java.util.stream.Collectors;

@DependOnResolvers(JarFileNamePluginResolver.class)
public class JarPropertiesNamePluginResolver extends AbstractPluginResolver {

    @Override
    public void resolve(PluginContext context) {
        List<String> filenames = context.get(JarPlugin.FILE_NAMES);
        List<String> propertiesNames = filenames.stream()
                .filter(it -> it.endsWith(".properties"))
                .collect(Collectors.toList());
        context.set(JarPlugin.PROPERTIES_NAMES, propertiesNames);
    }

    @Override
    public boolean support(PluginContext context) {
        return context.contains(JarPlugin.FILE_NAMES);
    }
}
