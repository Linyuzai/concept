package com.github.linyuzai.plugin.jar.resolver;

import com.github.linyuzai.plugin.core.context.PluginContext;
import com.github.linyuzai.plugin.core.resolver.AbstractPluginResolver;
import com.github.linyuzai.plugin.core.resolver.PluginResolverChain;
import com.github.linyuzai.plugin.core.resolver.annotation.DependOnResolver;
import com.github.linyuzai.plugin.jar.JarPlugin;

import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

@DependOnResolver(JarFileNamePluginResolver.class)
public class JarPropertiesNamePluginResolver extends AbstractPluginResolver {

    @Override
    public void resolve(PluginContext context, PluginResolverChain chain) {
        Collection<String> filenames = context.get(JarPlugin.FILE_NAMES);
        List<String> propertiesNames = filenames.stream()
                .filter(it -> it.endsWith(".properties"))
                .collect(Collectors.toList());
        context.set(JarPlugin.PROPERTIES_NAMES, propertiesNames);
        chain.next(context);
    }

    @Override
    public boolean support(PluginContext context) {
        return context.contains(JarPlugin.FILE_NAMES);
    }
}
