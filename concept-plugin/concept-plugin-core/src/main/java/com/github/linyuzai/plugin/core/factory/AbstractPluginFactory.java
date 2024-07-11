package com.github.linyuzai.plugin.core.factory;

import com.github.linyuzai.plugin.core.concept.Plugin;
import com.github.linyuzai.plugin.core.metadata.PluginMetadata;
import com.github.linyuzai.plugin.core.context.PluginContext;

public abstract class AbstractPluginFactory<T> implements PluginFactory {

    @Override
    public Plugin create(Object source, PluginContext context) {
        T supported = getSupported(source);
        if (supported == null) {
            return null;
        }
        PluginMetadata metadata = createMetadata(supported);
        if (metadata == null) {
            return null;
        }
        Plugin plugin = doCreate(supported, metadata, context);
        if (plugin == null) {
            return null;
        }
        plugin.setMetadata(metadata);
        return plugin;
    }

    protected PluginMetadata createMetadata(Object source) {
        return getMetadataFactory().create(source);
    }

    protected abstract T getSupported(Object source);

    protected abstract Plugin doCreate(T supported, PluginMetadata metadata, PluginContext context);
}
