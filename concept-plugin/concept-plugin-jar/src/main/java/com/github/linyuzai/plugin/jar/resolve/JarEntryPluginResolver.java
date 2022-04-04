package com.github.linyuzai.plugin.jar.resolve;

import com.github.linyuzai.plugin.core.context.PluginContext;
import com.github.linyuzai.plugin.core.resolve.PluginResolvedEvent;
import com.github.linyuzai.plugin.core.resolve.PluginResolver;
import com.github.linyuzai.plugin.jar.concept.JarPlugin;

import java.util.List;
import java.util.jar.JarEntry;
import java.util.stream.Collectors;

public class JarEntryPluginResolver implements PluginResolver {

    @Override
    public void resolve(PluginContext context) {
        JarPlugin plugin = context.getPlugin();
        List<JarEntry> entries = plugin.getFile()
                .stream().collect(Collectors.toList());
        context.set(JarPlugin.ENTRY, entries);
        context.publish(new PluginResolvedEvent(context, this, JarPlugin.ENTRY, entries));
    }

    @Override
    public boolean support(PluginContext context) {
        return true;
    }
}
