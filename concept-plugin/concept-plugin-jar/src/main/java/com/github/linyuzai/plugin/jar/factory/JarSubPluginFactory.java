package com.github.linyuzai.plugin.jar.factory;

import com.github.linyuzai.plugin.core.concept.Plugin;
import com.github.linyuzai.plugin.core.context.PluginContext;
import com.github.linyuzai.plugin.jar.concept.JarPlugin;
import com.github.linyuzai.plugin.jar.extension.ExJarPlugin;
import com.github.linyuzai.plugin.jar.extension.ExJarPluginEntry;
import com.github.linyuzai.plugin.zip.concept.ZipPlugin;
import com.github.linyuzai.plugin.zip.factory.ZipSubPluginFactory;

import java.net.URL;
import java.util.zip.ZipInputStream;

public class JarSubPluginFactory extends ZipSubPluginFactory {

    @Override
    protected Plugin createFromEntry(Plugin.Entry entry, URL url, PluginContext context) {
        if (entry instanceof ExJarPluginEntry) {
            try {
                return new ExJarPlugin(((ExJarPluginEntry) entry).getJarEntry().asJarFile());
            } catch (Throwable e) {
                return null;
            }
        }
        return super.createFromEntry(entry, url, context);
    }

    @Override
    protected ZipPlugin createZipPlugin(ZipInputStream zis, URL url, Plugin.Entry parent) {
        return new JarPlugin(zis, url, parent);
    }

    @Override
    protected boolean supportEntry(Plugin.Entry entry, PluginContext context) {
        return entry.getName().endsWith(".jar") || super.supportEntry(entry, context);
    }
}
