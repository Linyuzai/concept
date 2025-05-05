package com.github.linyuzai.plugin.jar.factory;

import com.github.linyuzai.plugin.core.concept.Plugin;
import com.github.linyuzai.plugin.core.context.PluginContext;
import com.github.linyuzai.plugin.core.factory.PluginFactory;
import com.github.linyuzai.plugin.core.metadata.PluginMetadata;
import com.github.linyuzai.plugin.jar.concept.JarPlugin;
import com.github.linyuzai.plugin.jar.concept.JarStreamPlugin;
import com.github.linyuzai.plugin.jar.extension.ExJarFile;
import com.github.linyuzai.plugin.jar.extension.ExJarPlugin;
import com.github.linyuzai.plugin.jar.extension.ExJarPluginEntry;
import com.github.linyuzai.plugin.zip.factory.ZipInputStreamPluginFactory;
import lombok.SneakyThrows;

import java.net.URL;
import java.util.zip.ZipInputStream;

import static com.github.linyuzai.plugin.zip.util.ZipUtils.SEPARATOR;

/**
 * zip流插件工厂
 */
public class JarInputStreamPluginFactory extends ZipInputStreamPluginFactory {

    @SneakyThrows
    @Override
    public Plugin create(Object source, PluginMetadata metadata, PluginContext context) {
        if (source instanceof Plugin.Entry) {
            if (source instanceof ExJarPluginEntry) {
                try {
                    ExJarFile file = ((ExJarPluginEntry) source).getJarEntry().asJarFile();
                    return new ExJarPlugin(file);
                } catch (Throwable e) {
                    return null;
                }
            } else {
                Plugin.Entry entry = (Plugin.Entry) source;
                if (entry.getName().endsWith(JarPlugin.SUFFIX)) {
                    Object id = entry.getId();
                    if (id instanceof URL) {
                        URL url = new URL((URL) id, entry.getName() + SEPARATOR);
                        ZipInputStream zis = new ZipInputStream(entry.getContent().getInputStream());
                        new JarStreamPlugin(zis, url, entry);
                    }
                }
            }
        }
        return null;
    }
}
