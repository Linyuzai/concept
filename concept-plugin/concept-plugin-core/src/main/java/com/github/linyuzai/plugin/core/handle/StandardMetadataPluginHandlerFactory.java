package com.github.linyuzai.plugin.core.handle;

import com.github.linyuzai.plugin.core.concept.Plugin;
import com.github.linyuzai.plugin.core.context.PluginContext;

public abstract class StandardMetadataPluginHandlerFactory<T extends Plugin.StandardMetadata>
        implements PluginHandlerFactory {

    @Override
    public PluginHandler create(PluginContext context) {
        T metadata = context.getPlugin().getMetadata().standard();
        return doCreate(metadata);
    }

    public abstract PluginHandler doCreate(T metadata);
}
