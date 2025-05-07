package com.github.linyuzai.plugin.jar.factory;

import com.github.linyuzai.plugin.core.concept.Plugin;
import com.github.linyuzai.plugin.core.context.PluginContext;
import com.github.linyuzai.plugin.core.exception.PluginException;
import com.github.linyuzai.plugin.core.metadata.PluginMetadata;
import com.github.linyuzai.plugin.jar.concept.JarPlugin;
import com.github.linyuzai.plugin.jar.concept.JarStreamPlugin;
import com.github.linyuzai.plugin.jar.extension.ExJarFile;
import com.github.linyuzai.plugin.jar.extension.ExJarPlugin;
import com.github.linyuzai.plugin.jar.extension.ExJarPluginEntry;
import com.github.linyuzai.plugin.zip.concept.ZipPlugin;
import lombok.SneakyThrows;

import java.io.IOException;
import java.net.URL;
import java.util.zip.ZipInputStream;

import static com.github.linyuzai.plugin.zip.util.ZipUtils.SEPARATOR;

/**
 * zip流插件工厂
 */
public class JarStreamPluginFactory extends JarPluginFactory {

    @SneakyThrows
    @Override
    protected Plugin create(Object source, PluginContext context) {
        if (source instanceof Plugin.Entry) {
            Plugin.Entry entry = (Plugin.Entry) source;
            if (entry.getName().endsWith(JarPlugin.SUFFIX) ||
                    entry.getName().endsWith(ZipPlugin.SUFFIX)) {
                Object id = entry.getId();
                if (id instanceof URL) {
                    URL url = new URL((URL) id, entry.getName() + SEPARATOR);
                    return new JarStreamPlugin(url, () -> {
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

    @Override
    protected boolean supportMode(String mode) {
        return JarPlugin.Mode.STREAM.equalsIgnoreCase(mode);
    }
}
