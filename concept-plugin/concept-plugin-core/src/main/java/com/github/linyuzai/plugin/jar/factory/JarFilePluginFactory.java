package com.github.linyuzai.plugin.jar.factory;

import com.github.linyuzai.plugin.core.concept.Plugin;
import com.github.linyuzai.plugin.core.concept.PluginConcept;
import com.github.linyuzai.plugin.core.factory.PluginFactory;
import com.github.linyuzai.plugin.jar.concept.JarPlugin;
import com.github.linyuzai.plugin.jar.extension.NestedJarFile;
import lombok.SneakyThrows;

import java.io.File;

/**
 * 支持 {@link File} 类型的 {@link JarPlugin} 工厂
 */
public class JarFilePluginFactory implements PluginFactory {

    @Override
    public boolean support(Object o, PluginConcept concept) {
        return o instanceof File;
    }

    @SneakyThrows
    @Override
    public Plugin create(Object o, PluginConcept concept) {
        return new JarPlugin(new NestedJarFile((File) o));
    }
}
