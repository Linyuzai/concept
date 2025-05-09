package com.github.linyuzai.plugin.jar.factory;

import com.github.linyuzai.plugin.core.concept.Plugin;
import com.github.linyuzai.plugin.core.context.PluginContext;
import com.github.linyuzai.plugin.core.exception.PluginException;
import com.github.linyuzai.plugin.core.metadata.PluginMetadata;
import com.github.linyuzai.plugin.jar.concept.JarPlugin;
import com.github.linyuzai.plugin.jar.concept.JarStreamPlugin;
import com.github.linyuzai.plugin.zip.concept.ZipPlugin;
import com.github.linyuzai.plugin.core.factory.PluginSourceProvider;
import com.github.linyuzai.plugin.zip.concept.ZipStreamPlugin;
import com.github.linyuzai.plugin.zip.factory.ZipStreamPluginFactory;
import lombok.SneakyThrows;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.function.Supplier;

import static com.github.linyuzai.plugin.zip.util.ZipUtils.SEPARATOR;

/**
 * zip流插件工厂
 */
public class JarStreamPluginFactory extends ZipStreamPluginFactory implements JarPluginFactory {

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
