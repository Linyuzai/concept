package com.github.linyuzai.plugin.jar.factory;

import com.github.linyuzai.plugin.core.concept.Plugin;
import com.github.linyuzai.plugin.core.concept.PluginConcept;
import com.github.linyuzai.plugin.core.metadata.PluginMetadata;
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
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.jar.JarFile;

@Getter
@Setter
public class JarPluginFactory extends ZipPluginFactory {

    private String defaultMode = JarPlugin.Mode.STREAM;

    @SneakyThrows
    @Override
    protected Plugin doCreate(File file, PluginMetadata metadata, PluginContext context, PluginConcept concept) {
        String mode = getMode(metadata);
        switch (mode.toUpperCase()) {
            case JarPlugin.Mode.FILE:
                return new ExJarPlugin(new ExJarFile(file));
            case JarPlugin.Mode.STREAM:
                return super.doCreate(file, metadata, context, concept);
            default:
                throw new PluginException("Plugin mode not supported");
        }
    }

    public String getMode(PluginMetadata metadata) {
        JarPlugin.StandardMetadata standard = metadata.asStandard();
        String mode = standard.getJar().getMode();
        return (mode == null || mode.isEmpty()) ? defaultMode : mode;
    }

    @Override
    protected JarFilePlugin createPlugin(File file, URL url) throws IOException {
        return new JarFilePlugin(new JarFile(file), url);
    }

    @Override
    protected URL getURL(File file) throws MalformedURLException {
        return PluginUtils.getURL(file);
    }
}
