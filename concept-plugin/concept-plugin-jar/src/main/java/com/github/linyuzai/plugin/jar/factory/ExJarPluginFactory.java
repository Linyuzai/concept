package com.github.linyuzai.plugin.jar.factory;

import com.github.linyuzai.plugin.core.concept.Plugin;
import com.github.linyuzai.plugin.core.context.PluginContext;
import com.github.linyuzai.plugin.core.factory.PluginFactory;
import com.github.linyuzai.plugin.core.metadata.PluginMetadata;
import com.github.linyuzai.plugin.jar.concept.JarPlugin;
import com.github.linyuzai.plugin.jar.extension.ExJarFile;
import com.github.linyuzai.plugin.jar.extension.ExJarPlugin;
import com.github.linyuzai.plugin.jar.extension.ExJarPluginEntry;
import com.github.linyuzai.plugin.zip.util.ZipUtils;
import lombok.SneakyThrows;

import java.io.File;

public class ExJarPluginFactory extends JarPluginFactory {

    @SneakyThrows
    @Override
    protected Plugin create(Object source, PluginContext context) {
        File file = ZipUtils.getFile(source, JarPlugin.SUFFIX);
        ExJarFile jarFile;
        if (file == null) {
            if (source instanceof ExJarPluginEntry) {
                jarFile = ((ExJarPluginEntry) source).getJarEntry().asJarFile();
            } else {
                jarFile = null;
            }
        } else {
            jarFile = new ExJarFile(file);
        }
        if (jarFile == null) {
            return null;
        } else {
            return new ExJarPlugin(jarFile);
        }
    }

    @Override
    protected boolean supportMode(String mode) {
        return JarPlugin.Mode.FILE.equalsIgnoreCase(mode);
    }
}
