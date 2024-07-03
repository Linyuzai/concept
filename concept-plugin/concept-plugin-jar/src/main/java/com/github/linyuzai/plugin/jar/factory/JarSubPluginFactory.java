package com.github.linyuzai.plugin.jar.factory;

import com.github.linyuzai.plugin.core.concept.Plugin;
import com.github.linyuzai.plugin.core.context.PluginContext;
import com.github.linyuzai.plugin.jar.concept.JarStreamPlugin;
import com.github.linyuzai.plugin.jar.extension.ExJarEntry;
import com.github.linyuzai.plugin.jar.extension.ExJarFile;
import com.github.linyuzai.plugin.jar.extension.ExJarPlugin;
import com.github.linyuzai.plugin.jar.extension.ExJarPluginEntry;
import com.github.linyuzai.plugin.zip.factory.ZipSubPluginFactory;

import java.net.URL;
import java.util.zip.ZipInputStream;

public class JarSubPluginFactory extends ZipSubPluginFactory {

    @Override
    protected Plugin createFromEntry(Plugin.Entry entry, URL url, PluginContext context) {
        if (entry instanceof ExJarPluginEntry) {
            try {
                ExJarFile file = ((ExJarPluginEntry) entry).getJarEntry().asJarFile();
                return new ExJarPlugin(file, entry);
            } catch (Throwable e) {
                return null;
            }
        }
        return super.createFromEntry(entry, url, context);
    }

    @Override
    protected JarStreamPlugin createZipPlugin(ZipInputStream zis, URL url, Plugin.Entry parent) {
        return new JarStreamPlugin(zis, url, parent);
    }

    @Override
    protected boolean supportEntry(Plugin.Entry entry, PluginContext context) {
        return entry.getName().endsWith(".jar") || super.supportEntry(entry, context);
    }
}
