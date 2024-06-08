package com.github.linyuzai.plugin.jar.factory;

import com.github.linyuzai.plugin.core.concept.Plugin;
import com.github.linyuzai.plugin.core.context.PluginContext;
import com.github.linyuzai.plugin.jar.concept.JarPlugin;
import com.github.linyuzai.plugin.jar.extension.NestedJarFile;
import com.github.linyuzai.plugin.zip.factory.ZipPluginFactory;
import lombok.SneakyThrows;

import java.io.File;
import java.net.MalformedURLException;
import java.net.URL;

public class JarPluginFactory extends ZipPluginFactory {

    @Override
    public String getMode(File file, Plugin.Metadata metadata, PluginContext context) {
        String mode = metadata.get("concept.plugin.jar.mode");
        if (mode != null) {
            return mode;
        }
        return super.getMode(file, metadata, context);
    }

    @SneakyThrows
    @Override
    public Plugin createInMode(File file, String mode, PluginContext context) {
        if (JarPlugin.Mode.NESTED.equalsIgnoreCase(mode)) {
            return new JarPlugin(new NestedJarFile(file));
        } else {
            return super.createInMode(file, mode, context);
        }
    }

    @Override
    protected boolean supportFile(File file, PluginContext context) {
        return file.getName().endsWith(".jar") || super.supportFile(file, context);
    }

    @Override
    protected URL getURL(File file) throws MalformedURLException {
        return new URL("jar", "", -1, file.getAbsolutePath());
    }
}
