package com.github.linyuzai.plugin.jar.resolver;

import com.github.linyuzai.plugin.core.context.PluginContext;
import com.github.linyuzai.plugin.core.resolver.AbstractPluginResolver;
import com.github.linyuzai.plugin.core.resolver.PluginResolverChain;
import com.github.linyuzai.plugin.core.resolver.dependence.DependOnResolver;
import com.github.linyuzai.plugin.jar.JarPlugin;

import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

@DependOnResolver(JarFileNamePluginResolver.class)
public class JarClassNamePluginResolver extends AbstractPluginResolver {

    @Override
    public void resolve(PluginContext context, PluginResolverChain chain) {
        Collection<String> filenames = context.get(JarPlugin.FILE_NAMES);
        List<String> classNames = filenames.stream()
                .filter(it -> it.endsWith(".class"))
                .map(it -> it.substring(0, it.lastIndexOf(".")).replaceAll("/", "."))
                .collect(Collectors.toList());
        context.set(JarPlugin.CLASS_NAMES, classNames);
        chain.next(context);
    }

    @Override
    public boolean support(PluginContext context) {
        return context.contains(JarPlugin.FILE_NAMES);
    }
}
