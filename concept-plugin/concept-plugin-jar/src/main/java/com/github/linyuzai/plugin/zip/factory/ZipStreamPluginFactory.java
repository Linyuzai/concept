package com.github.linyuzai.plugin.zip.factory;

import com.github.linyuzai.plugin.core.concept.Plugin;
import com.github.linyuzai.plugin.core.context.PluginContext;
import com.github.linyuzai.plugin.core.exception.PluginException;
import com.github.linyuzai.plugin.core.factory.PluginFactory;
import com.github.linyuzai.plugin.core.metadata.PluginMetadata;
import com.github.linyuzai.plugin.zip.concept.ZipPlugin;
import com.github.linyuzai.plugin.zip.concept.ZipStreamPlugin;
import com.github.linyuzai.plugin.core.factory.PluginSourceProvider;
import lombok.SneakyThrows;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.function.Supplier;

import static com.github.linyuzai.plugin.zip.util.ZipUtils.SEPARATOR;

/**
 * zip流插件工厂
 */
public class ZipStreamPluginFactory implements PluginFactory {

    @SneakyThrows
    @Override
    public Plugin create(Object source, PluginMetadata metadata, PluginContext context) {
        if (source instanceof PluginSourceProvider) {
            PluginSourceProvider provider = (PluginSourceProvider) source;
            URL url = new URL(provider.getKey());
            return create(url, new Supplier<InputStream>() {
                @SneakyThrows
                @Override
                public InputStream get() {
                    return provider.getInputStream();
                }
            });
        } else if (source instanceof Plugin.Entry) {
            Plugin.Entry entry = (Plugin.Entry) source;
            if (support(entry.getName())) {
                Object id = entry.getId();
                if (id instanceof URL) {
                    URL url = new URL((URL) id, entry.getName() + SEPARATOR);
                    return create(url, () -> {
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

    protected ZipStreamPlugin create(URL url, Supplier<InputStream> supplier) {
        return new ZipStreamPlugin(url, supplier);
    }

    protected boolean support(String name) {
        return name.endsWith(ZipPlugin.SUFFIX_ZIP);
    }
}
