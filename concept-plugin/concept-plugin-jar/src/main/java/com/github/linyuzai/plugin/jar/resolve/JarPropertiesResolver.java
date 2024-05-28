package com.github.linyuzai.plugin.jar.resolve;

import com.github.linyuzai.plugin.core.context.PluginContext;
import com.github.linyuzai.plugin.core.handle.HandlerDependency;
import com.github.linyuzai.plugin.core.resolve.PropertiesNameResolver;
import com.github.linyuzai.plugin.core.resolve.PropertiesResolver;
import com.github.linyuzai.plugin.jar.concept.JarPlugin;
import lombok.SneakyThrows;

import java.io.InputStream;
import java.util.Properties;
import java.util.zip.ZipEntry;

/**
 * jar 中 {@link Properties} 解析器
 */
@HandlerDependency(PropertiesNameResolver.class)
public class JarPropertiesResolver extends PropertiesResolver {

    /**
     * 通过 {@link java.util.jar.JarFile#getInputStream(ZipEntry)} 来加载 {@link Properties}
     *
     * @param context        上下文 {@link PluginContext}
     * @param propertiesName 名称
     * @return {@link Properties}
     */
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
