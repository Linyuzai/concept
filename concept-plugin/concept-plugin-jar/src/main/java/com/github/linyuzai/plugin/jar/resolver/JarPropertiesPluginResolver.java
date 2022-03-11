package com.github.linyuzai.plugin.jar.resolver;

import com.github.linyuzai.plugin.core.context.PluginContext;
import com.github.linyuzai.plugin.core.resolver.AbstractPluginResolver;
import com.github.linyuzai.plugin.core.resolver.dependence.DependOnResolvers;
import com.github.linyuzai.plugin.jar.JarPlugin;
import lombok.SneakyThrows;

import java.io.InputStream;
import java.util.Collection;
import java.util.List;
import java.util.Properties;
import java.util.stream.Collectors;
import java.util.zip.ZipEntry;

@DependOnResolvers(JarPropertiesNamePluginResolver.class)
public class JarPropertiesPluginResolver extends AbstractPluginResolver {

    @Override
    public void resolve(PluginContext context) {
        List<String> propertiesNames = context.get(JarPlugin.PROPERTIES_NAMES);
        List<Properties> properties = propertiesNames.stream()
                .map(it -> load(context, it))
                .collect(Collectors.toList());
        context.set(JarPlugin.PROPERTIES, properties);
    }

    @SneakyThrows
    private Properties load(PluginContext context, String propertiesName) {
        JarPlugin plugin = context.getPlugin();
        ZipEntry entry = plugin.getFile().getEntry(propertiesName);
        InputStream is = plugin.getFile().getInputStream(entry);
        Properties properties = new Properties();
        properties.load(is);
        try {
            is.close();
        } catch (Throwable ignore) {
        }
        return properties;
    }

    @Override
    public boolean support(PluginContext context) {
        return context.contains(JarPlugin.PROPERTIES_NAMES);
    }
}
