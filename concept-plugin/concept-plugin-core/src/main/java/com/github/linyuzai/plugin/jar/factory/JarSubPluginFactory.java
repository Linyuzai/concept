package com.github.linyuzai.plugin.jar.factory;

import com.github.linyuzai.plugin.core.concept.Plugin;
import com.github.linyuzai.plugin.core.context.PluginContext;
import com.github.linyuzai.plugin.jar.concept.JarPlugin;
import com.github.linyuzai.plugin.jar.concept.JarPluginEntry;
import com.github.linyuzai.plugin.zip.factory.ZipSubPluginFactory;
import lombok.SneakyThrows;

import java.net.URL;

public class JarSubPluginFactory extends ZipSubPluginFactory {

    @SneakyThrows
    @Override
    protected Plugin createFromEntry(Plugin.Entry entry, URL url, PluginContext context) {
        if (entry instanceof JarPluginEntry) {
            return new JarPlugin(((JarPluginEntry) entry).getJarEntry().asJarFile());
        }
        return super.createFromEntry(entry, url, context);
    }

    @Override
    protected boolean supportEntry(Plugin.Entry entry, PluginContext context) {
        return entry.getName().endsWith(".jar") || super.supportEntry(entry, context);
    }
}
