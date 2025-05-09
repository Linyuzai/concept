package com.github.linyuzai.plugin.jar.factory;

import com.github.linyuzai.plugin.core.concept.Plugin;
import com.github.linyuzai.plugin.core.context.PluginContext;
import com.github.linyuzai.plugin.core.factory.PluginFactory;
import com.github.linyuzai.plugin.core.metadata.PluginMetadata;
import com.github.linyuzai.plugin.core.util.PluginUtils;
import com.github.linyuzai.plugin.jar.concept.JarFilePlugin;
import com.github.linyuzai.plugin.jar.concept.JarPlugin;
import com.github.linyuzai.plugin.zip.util.ZipUtils;
import lombok.SneakyThrows;

import java.io.File;
import java.net.URL;
import java.util.jar.JarFile;

import static com.github.linyuzai.plugin.zip.util.ZipUtils.SEPARATOR;

public class JarFilePluginFactory extends ModePluginFactory implements JarPluginFactory {

    @SneakyThrows
    @Override
    public Plugin create(Object source, PluginMetadata metadata, PluginContext context) {
        String mode = getMode(metadata);
        if (JarPlugin.Mode.STREAM.equalsIgnoreCase(mode)) {
            File file = ZipUtils.getFile(source, getSupportedSuffixes());
            if (file == null) {
                return null;
            }
            URL url = PluginUtils.getURL(file.getName() + SEPARATOR);
            return new JarFilePlugin(new JarFile(file), url);
        }
        return null;
    }
}
