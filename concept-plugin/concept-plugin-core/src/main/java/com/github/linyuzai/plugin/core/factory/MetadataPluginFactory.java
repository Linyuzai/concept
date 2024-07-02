package com.github.linyuzai.plugin.core.factory;

import com.github.linyuzai.plugin.core.concept.Plugin;
import com.github.linyuzai.plugin.core.metadata.PluginMetadata;
import com.github.linyuzai.plugin.core.context.PluginContext;

public abstract class MetadataPluginFactory<T> implements PluginFactory {

    @Override
    public Plugin create(Object o, PluginContext context) {
        T source = getSource(o, context);
        if (source == null) {
            return null;
        }
        PluginMetadata metadata = createMetadata(source, context);
        if (metadata == null) {
            return null;
        }
        Plugin plugin = doCreate(source, metadata, context);
        plugin.setMetadata(metadata);
        return plugin;
    }

    public abstract Plugin doCreate(T o, PluginMetadata metadata, PluginContext context);

    protected abstract T getSource(Object o, PluginContext context);

    protected abstract PluginMetadata createMetadata(T o, PluginContext context);
}
