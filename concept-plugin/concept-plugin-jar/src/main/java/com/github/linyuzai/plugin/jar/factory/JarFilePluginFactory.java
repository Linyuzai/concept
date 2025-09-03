package com.github.linyuzai.plugin.jar.factory;

import com.github.linyuzai.plugin.core.concept.Plugin;
import com.github.linyuzai.plugin.core.concept.PluginDefinition;
import com.github.linyuzai.plugin.core.context.PluginContext;
import com.github.linyuzai.plugin.core.factory.PluginFactory;
import com.github.linyuzai.plugin.core.metadata.PluginMetadata;
import com.github.linyuzai.plugin.jar.concept.JarFilePlugin;
import com.github.linyuzai.plugin.jar.concept.JarPluginSuffixes;
import com.github.linyuzai.plugin.zip.util.ZipUtils;
import lombok.SneakyThrows;

import java.io.File;
import java.util.jar.JarFile;

public class JarFilePluginFactory implements PluginFactory, JarPluginSuffixes {

    @SneakyThrows
    @Override
    public Plugin create(PluginDefinition definition, PluginMetadata metadata, PluginContext context) {
        File file = ZipUtils.getFile(definition, getSupportedSuffixes());
        if (file == null) {
            return null;
        }
        return new JarFilePlugin(new JarFile(file));
    }
}
