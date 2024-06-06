package com.github.linyuzai.plugin.jar.factory;

import com.github.linyuzai.plugin.core.concept.Plugin;
import com.github.linyuzai.plugin.core.concept.PluginConcept;
import com.github.linyuzai.plugin.core.concept.PluginEntry;
import com.github.linyuzai.plugin.core.factory.SubPluginFactory;
import com.github.linyuzai.plugin.jar.concept.JarPlugin;
import com.github.linyuzai.plugin.jar.concept.JarPluginEntry;
import com.github.linyuzai.plugin.jar.extension.NestedJarFile;
import lombok.SneakyThrows;

/**
 * 支持文件路径的 {@link JarPlugin} 工厂
 */
public class JarEntryPluginFactory extends SubPluginFactory {

    @Override
    public boolean doSupport(PluginEntry entry, PluginConcept concept) {
        return entry instanceof JarPluginEntry && entry.getName().endsWith(".jar");
    }

    @SneakyThrows
    @Override
    public Plugin doCreate(PluginEntry entry, PluginConcept concept) {
        NestedJarFile jarFile = ((JarPluginEntry) entry).getJarEntry().asJarFile();
        return new JarPlugin(jarFile);
    }
}
