package com.github.linyuzai.plugin.jar.resolve;

import com.github.linyuzai.plugin.core.context.PluginContext;
import com.github.linyuzai.plugin.core.resolve.AbstractPluginResolver;
import com.github.linyuzai.plugin.core.resolve.DependOnResolvers;
import com.github.linyuzai.plugin.core.resolve.PathNamePluginResolver;
import com.github.linyuzai.plugin.jar.JarPlugin;

import java.io.File;
import java.util.List;
import java.util.jar.JarEntry;
import java.util.stream.Collectors;
import java.util.zip.ZipEntry;

@DependOnResolvers(JarEntryPluginResolver.class)
public class JarPathNamePluginResolver extends AbstractPluginResolver implements PathNamePluginResolver {

    @Override
    public void resolve(PluginContext context) {
        List<JarEntry> entries = context.get(JarPlugin.ENTRY);
        List<String> filenames = entries.stream()
                .map(ZipEntry::getName)
                .map(it -> it.replaceAll(File.separator, "/"))
                .collect(Collectors.toList());
        context.set(JarPlugin.PATH_NAME, filenames);
    }

    @Override
    public boolean support(PluginContext context) {
        return context.contains(JarPlugin.ENTRY);
    }
}
