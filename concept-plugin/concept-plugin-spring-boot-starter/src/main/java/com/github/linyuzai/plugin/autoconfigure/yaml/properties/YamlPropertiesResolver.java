package com.github.linyuzai.plugin.autoconfigure.yaml.properties;

import com.github.linyuzai.plugin.core.concept.Plugin;
import com.github.linyuzai.plugin.core.context.PluginContext;
import com.github.linyuzai.plugin.core.handle.HandlerDependency;
import com.github.linyuzai.plugin.core.handle.resolve.AbstractPluginResolver;
import com.github.linyuzai.plugin.core.handle.resolve.AbstractSupplier;
import com.github.linyuzai.plugin.core.handle.resolve.EntryResolver;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.springframework.beans.factory.config.YamlPropertiesFactoryBean;
import org.springframework.core.io.InputStreamResource;
import org.yaml.snakeyaml.Yaml;

import java.io.InputStream;

/**
 * {@link Yaml} 解析器
 */
@HandlerDependency(EntryResolver.class)
public class YamlPropertiesResolver extends AbstractPluginResolver<Plugin.Entry, YamlPropertiesSupplier> {

    /**
     * 过滤名称后缀.yaml .yml
     */
    @Override
    public boolean doFilter(Plugin.Entry entry, PluginContext context) {
        return entry.getName().endsWith(".yaml") || entry.getName().endsWith(".yml");
    }

    @Override
    public YamlPropertiesSupplier doResolve(Plugin.Entry entry, PluginContext context) {
        return new YamlPropertiesSupplierImpl(entry.getContent());
    }

    @Override
    public Object getInboundKey() {
        return Plugin.Entry.class;
    }

    @Override
    public Object getOutboundKey() {
        return YamlPropertiesSupplier.class;
    }

    @RequiredArgsConstructor
    public static class YamlPropertiesSupplierImpl extends AbstractSupplier<YamlPropertiesFactoryBean> implements YamlPropertiesSupplier {

        private final Plugin.Content content;

        @SneakyThrows
        @Override
        public YamlPropertiesFactoryBean create() {
            YamlPropertiesFactoryBean factoryBean = new YamlPropertiesFactoryBean();
            if (content == null) {
                return factoryBean;
            }
            try (InputStream is = content.getInputStream()) {
                factoryBean.setResources(new InputStreamResource(is));
                return factoryBean;
            }
        }
    }
}
