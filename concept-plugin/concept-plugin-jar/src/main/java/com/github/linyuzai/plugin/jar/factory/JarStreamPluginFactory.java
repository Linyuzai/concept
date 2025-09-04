package com.github.linyuzai.plugin.jar.factory;

import com.github.linyuzai.plugin.core.concept.PluginDefinition;
import com.github.linyuzai.plugin.jar.concept.JarPlugin;
import com.github.linyuzai.plugin.jar.concept.JarStreamPlugin;
import com.github.linyuzai.plugin.zip.factory.ZipStreamPluginFactory;

import java.io.InputStream;
import java.util.function.Supplier;

/**
 * jar流插件工厂
 */
public class JarStreamPluginFactory extends ZipStreamPluginFactory {

    @Override
    protected JarStreamPlugin create(Supplier<InputStream> supplier) {
        return new JarStreamPlugin(supplier);
    }

    @Override
    protected boolean support(PluginDefinition definition) {
        return definition.getPath().endsWith(JarPlugin.SUFFIX_JAR) || super.support(definition);
    }
}
