package com.github.linyuzai.plugin.jar.factory;

import com.github.linyuzai.plugin.core.concept.Plugin;
import com.github.linyuzai.plugin.core.context.PluginContext;
import com.github.linyuzai.plugin.core.exception.PluginException;
import com.github.linyuzai.plugin.core.util.PluginUtils;
import com.github.linyuzai.plugin.jar.concept.JarFilePlugin;
import com.github.linyuzai.plugin.jar.concept.JarPlugin;
import com.github.linyuzai.plugin.jar.extension.ExJarFile;
import com.github.linyuzai.plugin.jar.extension.ExJarPlugin;
import com.github.linyuzai.plugin.zip.factory.ZipPluginFactory;
import lombok.Getter;
import lombok.Setter;
import lombok.SneakyThrows;

import java.io.File;
import java.net.MalformedURLException;
import java.net.URL;

@Getter
@Setter
public class JarPluginFactory extends ZipPluginFactory {

    private String defaultMode = JarPlugin.Mode.STREAM;

    @SneakyThrows
    @Override
    public Plugin doCreate(File file, Plugin.Metadata metadata, PluginContext context) {
        String mode = getMode(file, metadata, context);
        switch (mode.toUpperCase()) {
            case JarPlugin.Mode.FILE:
                return new ExJarPlugin(new ExJarFile(file));
            case JarPlugin.Mode.STREAM:
                return super.doCreate(file, metadata, context);
            default:
                throw new PluginException("Plugin mode not supported");
        }
    }

    public String getMode(File file, Plugin.Metadata metadata, PluginContext context) {
        return metadata.get(JarPlugin.Mode.PropertyKey.MODE, defaultMode);
    }

    @Override
    protected JarFilePlugin createZipPlugin(File file, URL url) {
        return new JarFilePlugin(file, url);
    }

    @Override
    protected boolean supportFile(File file, PluginContext context) {
        return file.getName().endsWith(".jar") || super.supportFile(file, context);
    }

    @Override
    protected URL getURL(File file) throws MalformedURLException {
        return PluginUtils.getURL(file);
    }
}
