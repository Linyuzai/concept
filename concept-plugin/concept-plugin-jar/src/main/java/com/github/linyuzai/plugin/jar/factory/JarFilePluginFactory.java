package com.github.linyuzai.plugin.jar.factory;

import com.github.linyuzai.plugin.core.concept.Plugin;
import com.github.linyuzai.plugin.core.context.PluginContext;
import com.github.linyuzai.plugin.core.exception.PluginException;
import com.github.linyuzai.plugin.core.metadata.PluginMetadata;
import com.github.linyuzai.plugin.core.util.PluginUtils;
import com.github.linyuzai.plugin.jar.concept.JarFilePlugin;
import com.github.linyuzai.plugin.jar.concept.JarPlugin;
import com.github.linyuzai.plugin.jar.extension.ExJarFile;
import com.github.linyuzai.plugin.jar.extension.ExJarPlugin;
import com.github.linyuzai.plugin.zip.concept.ZipPlugin;
import com.github.linyuzai.plugin.zip.factory.ZipFilePluginFactory;
import com.github.linyuzai.plugin.zip.util.ZipUtils;
import lombok.Getter;
import lombok.Setter;
import lombok.SneakyThrows;

import java.io.File;
import java.net.URL;
import java.util.jar.JarFile;

import static com.github.linyuzai.plugin.zip.util.ZipUtils.SEPARATOR;

public class JarFilePluginFactory extends JarPluginFactory {

    @SneakyThrows
    @Override
    public Plugin create(Object source, PluginMetadata metadata, PluginContext context) {
        String mode = getMode(metadata);
        if (JarPlugin.Mode.STREAM.equalsIgnoreCase(mode)) {
            File file = ZipUtils.getFile(source, JarPlugin.SUFFIX, ZipPlugin.SUFFIX);
            if (file == null) {
                return null;
            }
            URL url = PluginUtils.getURL(file.getName() + SEPARATOR);
            new JarFilePlugin(new JarFile(file), url);
        }
        return null;
    }
}
