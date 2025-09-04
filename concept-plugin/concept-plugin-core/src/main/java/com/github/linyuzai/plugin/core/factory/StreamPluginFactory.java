package com.github.linyuzai.plugin.core.factory;

import com.github.linyuzai.plugin.core.concept.Plugin;
import com.github.linyuzai.plugin.core.concept.PluginDefinition;
import com.github.linyuzai.plugin.core.context.PluginContext;
import com.github.linyuzai.plugin.core.metadata.PluginMetadata;
import lombok.SneakyThrows;

import java.io.InputStream;
import java.util.function.Supplier;

/**
 * 流插件工厂
 */
public abstract class StreamPluginFactory extends AbstractPluginFactory {

    @SneakyThrows
    @Override
    public Plugin create(PluginDefinition definition, PluginMetadata metadata, PluginContext context) {
        if (support(definition)) {
            return create(definition::getInputStream);
        }
        return null;
    }

    protected abstract Plugin create(Supplier<InputStream> supplier);

    protected abstract boolean support(PluginDefinition definition);
}
