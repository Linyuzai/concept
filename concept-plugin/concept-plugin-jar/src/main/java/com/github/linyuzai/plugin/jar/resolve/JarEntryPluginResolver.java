package com.github.linyuzai.plugin.jar.resolve;

import com.github.linyuzai.plugin.core.context.PluginContext;
import com.github.linyuzai.plugin.core.resolve.AbstractPluginResolver;
import com.github.linyuzai.plugin.jar.JarPlugin;

import java.util.List;
import java.util.jar.JarEntry;
import java.util.stream.Collectors;

public class JarEntryPluginResolver extends AbstractPluginResolver {

    @Override
    public void resolve(PluginContext context) {
        JarPlugin plugin = context.getPlugin();
        List<JarEntry> entries = plugin.getFile()
                .stream().collect(Collectors.toList());
        context.set(JarPlugin.ENTRIES, entries);
    }

    @Override
    public boolean support(PluginContext context) {
        return true;
    }
}
