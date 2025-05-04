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
import lombok.Getter;
import lombok.Setter;
import lombok.SneakyThrows;

import java.io.File;
import java.util.jar.JarFile;
import java.util.zip.ZipFile;

@Getter
@Setter
public class JarFilePluginFactory extends ZipFilePluginFactory {

    private String defaultMode = JarPlugin.Mode.STREAM;

    @SneakyThrows
    @Override
    protected Plugin doCreate(ZipFile file, PluginMetadata metadata, PluginContext context) {
        if (file instanceof ExJarFile) {
            return new ExJarPlugin((ExJarFile) file);
        } else if (file instanceof JarFile) {
            return new JarFilePlugin((JarFile) file, PluginUtils.getURL(file.getName() + "!/"));
        } else {
            return super.doCreate(file, metadata, context);
        }
    }

    @SneakyThrows
    @Override
    protected JarFile parseSource(Object source, PluginMetadata metadata, PluginContext context) {
        File file = getFile(source, ".jar");
        if (file == null) {
            return null;
        }
        String mode = getMode(metadata);
        switch (mode.toUpperCase()) {
            case JarPlugin.Mode.FILE:
                return new ExJarFile(file);
            case JarPlugin.Mode.STREAM:
                return new JarFile(file);
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
