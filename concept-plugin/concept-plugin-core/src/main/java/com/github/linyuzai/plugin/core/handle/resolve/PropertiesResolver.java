package com.github.linyuzai.plugin.core.handle.resolve;

import com.github.linyuzai.plugin.core.concept.Plugin;
import com.github.linyuzai.plugin.core.context.PluginContext;
import com.github.linyuzai.plugin.core.handle.HandlerDependency;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.Map;
import java.util.Properties;

/**
 * {@link Properties} 解析器
 */
@HandlerDependency(EntryResolver.class)
public class PropertiesResolver extends AbstractPluginResolver<Plugin.Entry, PropertiesSupplier> {

    @Override
    public boolean doFilter(Plugin.Entry entry, PluginContext context) {
        return entry.getName().endsWith(".properties");
    }

    /**
     * 将所有名称对象的文件都加载到 {@link Properties} 对象中
     *
     * @param entry   插件项
     * @param context 上下文 {@link PluginContext}
     * @return {@link Properties} 的 {@link Map}
     */
    @Override
    public PropertiesSupplier doResolve(Plugin.Entry entry, PluginContext context) {
        return new PropertiesSupplierImpl(entry.getContent());
    }

    @Override
    public Object getInboundKey() {
        return Plugin.Entry.class;
    }

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
            try (InputStream is = content.getInputStream()) {
                Properties properties = new Properties();
                properties.load(new InputStreamReader(is, StandardCharsets.UTF_8));
                return properties;
            }
        }
    }
}
