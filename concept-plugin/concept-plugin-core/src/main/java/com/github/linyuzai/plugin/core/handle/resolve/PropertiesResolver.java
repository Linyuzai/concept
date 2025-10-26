package com.github.linyuzai.plugin.core.handle.resolve;

import com.github.linyuzai.plugin.core.concept.Plugin;
import com.github.linyuzai.plugin.core.context.PluginContext;
import com.github.linyuzai.plugin.core.handle.HandlerDependency;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.Properties;

/**
 * {@link Properties} 解析器
 */
@HandlerDependency(EntryResolver.class)
public class PropertiesResolver extends AbstractPluginResolver<Plugin.Entry, PropertiesSupplier> {

    /**
     * 过滤名称后缀.properties
     */
    @Override
    public boolean doFilter(Plugin.Entry entry, PluginContext context) {
        return entry.getName().endsWith(".properties");
    }

    @Override
    public PropertiesSupplier doResolve(Plugin.Entry entry, PluginContext context) {
        return new PropertiesSupplierImpl(entry.getContent());
    }

    /**
     * 解析插件项
     */
    @Override
    public Object getInboundKey() {
        return Plugin.Entry.class;
    }

    /**
     * 解析成Properties
     */
    @Override
    public Object getOutboundKey() {
        return PropertiesSupplier.class;
    }

    @RequiredArgsConstructor
    public static class PropertiesSupplierImpl extends AbstractSupplier<Properties> implements PropertiesSupplier {

        private final Plugin.Content content;

        @SneakyThrows
        @Override
        public Properties create() {
            Properties properties = new Properties();
            if (content == null) {
                return properties;
            }
            try (InputStream is = content.getInputStream()) {
                properties.load(new InputStreamReader(is, StandardCharsets.UTF_8));
                return properties;
            }
        }
    }
}
