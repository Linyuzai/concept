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
import com.github.linyuzai.plugin.zip.factory.ZipFilePluginFactory;
import com.github.linyuzai.plugin.zip.util.ZipUtils;
import lombok.Getter;
import lombok.Setter;
import lombok.SneakyThrows;

import java.io.File;
import java.net.URL;
import java.util.jar.JarFile;

import static com.github.linyuzai.plugin.zip.util.ZipUtils.SEPARATOR;

@Getter
@Setter
public class JarFilePluginFactory extends ZipFilePluginFactory {

    private String defaultMode = JarPlugin.Mode.STREAM;

    @SneakyThrows
    @Override
    public Plugin create(Object source, PluginMetadata metadata, PluginContext context) {
        File file = ZipUtils.getFile(source, JarPlugin.SUFFIX);
        if (file == null) {
            return null;
        }
        String mode = getMode(metadata);
        switch (mode.toUpperCase()) {
            case JarPlugin.Mode.FILE:
                new ExJarPlugin(new ExJarFile(file));
            case JarPlugin.Mode.STREAM:
                URL url = PluginUtils.getURL(file.getName() + SEPARATOR);
                new JarFilePlugin(new JarFile(file), url);
            default:
                throw new PluginException("Plugin mode not supported: " + mode);
        }
    }

    public String getMode(PluginMetadata metadata) {
        JarPlugin.StandardMetadata standard = metadata.asStandard();
        String mode = standard.getJar().getMode();
        return (mode == null || mode.isEmpty()) ? defaultMode : mode;
    }
}
