package com.github.linyuzai.plugin.jar.resolve;

import com.github.linyuzai.plugin.core.context.PluginContext;
import com.github.linyuzai.plugin.core.resolve.AbstractPluginResolver;
import com.github.linyuzai.plugin.core.resolve.DependOnResolvers;
import com.github.linyuzai.plugin.core.resolve.FilePathNamePluginResolver;
import com.github.linyuzai.plugin.jar.JarPlugin;

import java.util.List;
import java.util.jar.JarEntry;
import java.util.stream.Collectors;
import java.util.zip.ZipEntry;

@DependOnResolvers(JarEntryPluginResolver.class)
public class JarFilePathNamePluginResolver extends AbstractPluginResolver implements FilePathNamePluginResolver {

    @Override
    public void resolve(PluginContext context) {
        List<JarEntry> entries = context.get(JarPlugin.ENTRIES);
        List<String> filenames = entries.stream()
                .map(ZipEntry::getName)
                .collect(Collectors.toList());
        context.set(JarPlugin.FILE_NAMES, filenames);
    }

    @Override
    public boolean support(PluginContext context) {
        return context.contains(JarPlugin.ENTRIES);
    }
}
