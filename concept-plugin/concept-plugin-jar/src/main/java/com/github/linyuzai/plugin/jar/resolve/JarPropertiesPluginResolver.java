package com.github.linyuzai.plugin.jar.resolve;

import com.github.linyuzai.plugin.core.context.PluginContext;
import com.github.linyuzai.plugin.core.resolve.DependOnResolvers;
import com.github.linyuzai.plugin.core.resolve.PropertiesNamePluginResolver;
import com.github.linyuzai.plugin.core.resolve.PropertiesPluginResolver;
import com.github.linyuzai.plugin.jar.JarPlugin;
import lombok.SneakyThrows;

import java.io.InputStream;
import java.util.Properties;
import java.util.zip.ZipEntry;

@DependOnResolvers(PropertiesNamePluginResolver.class)
public class JarPropertiesPluginResolver extends PropertiesPluginResolver {

    @SneakyThrows
    @Override
    public Properties load(PluginContext context, String propertiesName) {
        JarPlugin plugin = context.getPlugin();
        ZipEntry entry = plugin.getFile().getEntry(propertiesName);
        try (InputStream is = plugin.getFile().getInputStream(entry)) {
            Properties properties = new Properties();
            properties.load(is);
            return properties;
        }
    }
}
