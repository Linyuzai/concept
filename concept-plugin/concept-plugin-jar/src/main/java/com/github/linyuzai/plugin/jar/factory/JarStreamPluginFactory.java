package com.github.linyuzai.plugin.jar.factory;

import com.github.linyuzai.plugin.jar.concept.JarPluginSuffixes;
import com.github.linyuzai.plugin.jar.concept.JarStreamPlugin;
import com.github.linyuzai.plugin.zip.factory.ZipStreamPluginFactory;

import java.io.InputStream;
import java.net.URL;
import java.util.function.Supplier;

/**
 * zip流插件工厂
 */
public class JarStreamPluginFactory extends ZipStreamPluginFactory implements JarPluginSuffixes {

    @Override
    protected JarStreamPlugin create(URL url, Supplier<InputStream> supplier) {
        return new JarStreamPlugin(url, supplier);
    }

    @Override
    protected boolean support(String name) {
        for (String suffix : getSupportedSuffixes()) {
            if (name.endsWith(suffix)) {
                return true;
            }
        }
        return false;
    }
}
