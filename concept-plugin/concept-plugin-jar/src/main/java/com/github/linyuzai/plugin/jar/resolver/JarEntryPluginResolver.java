package com.github.linyuzai.plugin.jar.resolver;

import com.github.linyuzai.plugin.core.context.PluginContext;
import com.github.linyuzai.plugin.core.resolver.AbstractPluginResolver;
import com.github.linyuzai.plugin.core.resolver.PluginResolverChain;
import com.github.linyuzai.plugin.jar.JarPlugin;

import java.util.List;
import java.util.jar.JarEntry;
import java.util.stream.Collectors;

public class JarEntryPluginResolver extends AbstractPluginResolver {

    @Override
    public void resolve(PluginContext context, PluginResolverChain chain) {
        JarPlugin plugin = context.getPlugin();
        List<JarEntry> entries = plugin.getFile()
                .stream().collect(Collectors.toList());
        context.set(JarPlugin.ENTRIES, entries);
        chain.next(context);
    }

    @Override
    public boolean support(PluginContext context) {
        return true;
    }
}
