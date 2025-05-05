package com.github.linyuzai.plugin.zip.factory;

import com.github.linyuzai.plugin.core.concept.Plugin;
import com.github.linyuzai.plugin.core.context.PluginContext;
import com.github.linyuzai.plugin.core.factory.PluginFactory;
import com.github.linyuzai.plugin.core.metadata.PluginMetadata;
import com.github.linyuzai.plugin.zip.concept.ZipPlugin;
import com.github.linyuzai.plugin.zip.concept.ZipStreamPlugin;
import com.github.linyuzai.plugin.zip.util.ZipUtils;
import lombok.SneakyThrows;

import java.net.URL;
import java.util.zip.ZipInputStream;

import static com.github.linyuzai.plugin.zip.util.ZipUtils.SEPARATOR;

/**
 * zip流插件工厂
 */
public class ZipInputStreamPluginFactory implements PluginFactory {

    @SneakyThrows
    @Override
    public Plugin create(Object source, PluginMetadata metadata, PluginContext context) {
        if (source instanceof Plugin.Entry) {
            Plugin.Entry entry = (Plugin.Entry) source;
            if (entry.getName().endsWith(ZipPlugin.SUFFIX)) {
                Object id = entry.getId();
                if (id instanceof URL) {
                    URL url = new URL((URL) id, entry.getName() + SEPARATOR);
                    ZipInputStream zis = new ZipInputStream(entry.getContent().getInputStream());
                    new ZipStreamPlugin(zis, url, entry);
                }

            }
        }
        return null;
    }
}
