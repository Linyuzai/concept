package com.github.linyuzai.plugin.jar.factory;

import com.github.linyuzai.plugin.core.concept.Plugin;
import com.github.linyuzai.plugin.core.context.PluginContext;
import com.github.linyuzai.plugin.core.metadata.PluginMetadata;
import com.github.linyuzai.plugin.jar.concept.JarPlugin;
import com.github.linyuzai.plugin.jar.concept.JarPluginSuffixes;
import com.github.linyuzai.plugin.jar.extension.ExJarFile;
import com.github.linyuzai.plugin.jar.extension.ExJarPlugin;
import com.github.linyuzai.plugin.jar.extension.ExJarPluginEntry;
import com.github.linyuzai.plugin.zip.util.ZipUtils;
import lombok.SneakyThrows;

import java.io.File;

public class ExJarPluginFactory extends ModePluginFactory implements JarPluginSuffixes {

    @SneakyThrows
    @Override
    public Plugin create(Object source, PluginMetadata metadata, PluginContext context) {
        if (source instanceof ExJarPluginEntry) {
            ExJarFile jarFile = ((ExJarPluginEntry) source).getJarEntry().asJarFile();
            return new ExJarPlugin(jarFile);
        }
        String mode = getMode(metadata);
        if (JarPlugin.Mode.FILE.equalsIgnoreCase(mode)) {
            File file = ZipUtils.getFile(source, getSupportedSuffixes());
            if (file == null) {
                return null;
            } else {
                return new ExJarPlugin(new ExJarFile(file));
            }
        }
        return null;
    }
}
