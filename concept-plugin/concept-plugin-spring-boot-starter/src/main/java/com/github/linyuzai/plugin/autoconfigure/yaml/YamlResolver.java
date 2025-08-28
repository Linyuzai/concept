package com.github.linyuzai.plugin.autoconfigure.yaml;

import com.github.linyuzai.plugin.core.concept.Plugin;
import com.github.linyuzai.plugin.core.context.PluginContext;
import com.github.linyuzai.plugin.core.handle.HandlerDependency;
import com.github.linyuzai.plugin.core.handle.resolve.AbstractPluginResolver;
import com.github.linyuzai.plugin.core.handle.resolve.AbstractSupplier;
import com.github.linyuzai.plugin.core.handle.resolve.EntryResolver;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.yaml.snakeyaml.Yaml;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;

/**
 * {@link Yaml} 解析器
 */
@HandlerDependency(EntryResolver.class)
public class YamlResolver extends AbstractPluginResolver<Plugin.Entry, YamlSupplier> {

    /**
     * 过滤名称后缀.yaml .yml
     */
    @Override
    public boolean doFilter(Plugin.Entry entry, PluginContext context) {
        return entry.getName().endsWith(".yaml") || entry.getName().endsWith(".yml");
    }

    @Override
    public YamlSupplier doResolve(Plugin.Entry entry, PluginContext context) {
        return new YamlSupplierImpl(entry.getContent());
    }

    @Override
    public Object getInboundKey() {
        return Plugin.Entry.class;
    }

    @Override
    public Object getOutboundKey() {
        return YamlSupplier.class;
    }

    @RequiredArgsConstructor
    public static class YamlSupplierImpl extends AbstractSupplier<Yaml> implements YamlSupplier {

        private final Plugin.Content content;

        @SneakyThrows
        @Override
        public Yaml create() {
            try (InputStream is = content.getInputStream()) {
                Yaml yaml = newYaml();
                yaml.load(new InputStreamReader(is, StandardCharsets.UTF_8));
                return yaml;
            }
        }

        protected Yaml newYaml() {
            return new Yaml();
        }
    }
}
