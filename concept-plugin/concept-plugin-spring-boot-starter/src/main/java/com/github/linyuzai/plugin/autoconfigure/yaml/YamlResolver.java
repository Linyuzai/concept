package com.github.linyuzai.plugin.autoconfigure.yaml;

import com.github.linyuzai.plugin.core.concept.Plugin;
import com.github.linyuzai.plugin.core.context.PluginContext;
import com.github.linyuzai.plugin.core.handle.HandlerDependency;
import com.github.linyuzai.plugin.core.handle.resolve.AbstractSupplier;
import com.github.linyuzai.plugin.core.handle.resolve.EntryResolver;
import com.github.linyuzai.plugin.core.handle.resolve.PropertiesResolver;
import com.github.linyuzai.plugin.core.handle.resolve.PropertiesSupplier;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.springframework.beans.factory.config.YamlPropertiesFactoryBean;
import org.springframework.core.io.InputStreamResource;

import java.io.InputStream;
import java.util.Properties;

/**
 * Yaml 解析器
 */
@HandlerDependency(EntryResolver.class)
public class YamlResolver extends PropertiesResolver {

    /**
     * 过滤名称后缀.yaml .yml
     */
    @Override
    public boolean doFilter(Plugin.Entry entry, PluginContext context) {
        return isYaml(entry) || super.doFilter(entry, context);
    }

    @Override
    public PropertiesSupplier doResolve(Plugin.Entry entry, PluginContext context) {
        if (isYaml(entry)) {
            return new YamlPropertiesSupplierImpl(entry.getContent());
        } else {
            return super.doResolve(entry, context);
        }
    }

    protected boolean isYaml(Plugin.Entry entry) {
        return entry.getName().endsWith(".yaml") || entry.getName().endsWith(".yml");
    }

    @RequiredArgsConstructor
    public static class YamlPropertiesSupplierImpl extends AbstractSupplier<Properties>
            implements PropertiesSupplier {

        private final Plugin.Content content;

        @SneakyThrows
        @Override
        public Properties create() {
            if (content == null) {
                return new Properties();
            }
            try (InputStream is = content.getInputStream()) {
                YamlPropertiesFactoryBean factoryBean = new YamlPropertiesFactoryBean();
                factoryBean.setResources(new InputStreamResource(is));
                return factoryBean.getObject();
            }
        }
    }
}
