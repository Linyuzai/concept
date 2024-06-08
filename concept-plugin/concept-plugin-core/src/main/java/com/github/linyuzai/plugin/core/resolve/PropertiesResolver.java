package com.github.linyuzai.plugin.core.resolve;

import com.github.linyuzai.plugin.core.concept.Plugin;
import com.github.linyuzai.plugin.core.context.PluginContext;
import com.github.linyuzai.plugin.core.handle.HandlerDependency;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;

import java.io.InputStream;
import java.util.Map;
import java.util.Properties;
import java.util.function.Supplier;

/**
 * {@link Properties} 解析器
 */
@HandlerDependency(EntryResolver.class)
public class PropertiesResolver extends AbstractPluginResolver<Plugin.Entry, Supplier<Properties>> {

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
    public Supplier<Properties> doResolve(Plugin.Entry entry, PluginContext context) {
        return new PropertiesContent(entry.getContent());
    }

    @Override
    public Object getInboundKey() {
        return Plugin.Entry.class;
    }

    @Override
    public Object getOutboundKey() {
        return Properties.class;
    }

    @RequiredArgsConstructor
    public static class PropertiesContent implements Supplier<Properties> {

        private final Plugin.Content content;

        private volatile Properties properties;

        @Override
        public Properties get() {
            if (properties == null) {
                synchronized (this) {
                    if (properties == null) {
                        properties = load();
                    }
                }
            }
            return properties;
        }

        @SneakyThrows
        protected Properties load() {
            try (InputStream is = content.getInputStream()) {
                Properties properties = new Properties();
                properties.load(is);
                return properties;
            }
        }
    }
}
