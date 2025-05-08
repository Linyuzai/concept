package com.github.linyuzai.plugin.zip.factory;

import com.github.linyuzai.plugin.core.concept.Plugin;
import com.github.linyuzai.plugin.core.context.PluginContext;
import com.github.linyuzai.plugin.core.exception.PluginException;
import com.github.linyuzai.plugin.core.factory.PluginFactory;
import com.github.linyuzai.plugin.core.metadata.PluginMetadata;
import com.github.linyuzai.plugin.jar.concept.JarStreamPlugin;
import com.github.linyuzai.plugin.zip.concept.ZipPlugin;
import com.github.linyuzai.plugin.zip.concept.ZipStreamPlugin;
import com.github.linyuzai.plugin.zip.concept.ZipStreamProvider;
import lombok.SneakyThrows;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.function.Supplier;
import java.util.zip.ZipInputStream;

import static com.github.linyuzai.plugin.zip.util.ZipUtils.SEPARATOR;

/**
 * zip流插件工厂
 */
public class ZipStreamPluginFactory implements PluginFactory {

    @SneakyThrows
    @Override
    public Plugin create(Object source, PluginMetadata metadata, PluginContext context) {
        if (source instanceof ZipStreamProvider) {
            ZipStreamProvider provider = (ZipStreamProvider) source;
            return new JarStreamPlugin(provider.getURL(), new Supplier<InputStream>() {
                @SneakyThrows
                @Override
                public InputStream get() {
                    return provider.getInputStream();
                }
            });
        } else if (source instanceof Plugin.Entry) {
            Plugin.Entry entry = (Plugin.Entry) source;
            if (entry.getName().endsWith(ZipPlugin.SUFFIX)) {
                Object id = entry.getId();
                if (id instanceof URL) {
                    URL url = new URL((URL) id, entry.getName() + SEPARATOR);
                    return new ZipStreamPlugin(url, () -> {
                        try {
                            return entry.getContent().getInputStream();
                        } catch (IOException e) {
                            throw new PluginException("Read Plugin Stream Error", e);
                        }
                    });
                }
            }
        }
        return null;
    }
}
