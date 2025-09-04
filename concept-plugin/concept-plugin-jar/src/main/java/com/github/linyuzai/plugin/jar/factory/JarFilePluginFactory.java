package com.github.linyuzai.plugin.jar.factory;

import com.github.linyuzai.plugin.core.concept.Plugin;
import com.github.linyuzai.plugin.core.concept.PluginDefinition;
import com.github.linyuzai.plugin.jar.concept.JarFilePlugin;
import com.github.linyuzai.plugin.jar.concept.JarPlugin;
import com.github.linyuzai.plugin.zip.factory.ZipFilePluginFactory;
import lombok.SneakyThrows;

import java.io.File;
import java.util.jar.JarFile;

/**
 * jar文件插件工厂
 */
public class JarFilePluginFactory extends ZipFilePluginFactory {

    @SneakyThrows
    @Override
    protected Plugin create(File file) {
        return new JarFilePlugin(new JarFile(file));
    }

    @Override
    protected boolean support(PluginDefinition definition) {
        return definition.getPath().endsWith(JarPlugin.SUFFIX_JAR) || super.support(definition);
    }
}
