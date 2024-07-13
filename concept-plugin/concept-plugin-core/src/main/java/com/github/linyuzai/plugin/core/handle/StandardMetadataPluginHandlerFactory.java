package com.github.linyuzai.plugin.core.handle;

import com.github.linyuzai.plugin.core.concept.Plugin;
import com.github.linyuzai.plugin.core.concept.PluginConcept;
import com.github.linyuzai.plugin.core.context.PluginContext;

@Deprecated
public abstract class StandardMetadataPluginHandlerFactory<T extends Plugin.StandardMetadata>
        implements PluginHandlerFactory {

    @Override
    public PluginHandler create(Plugin plugin, PluginContext context, PluginConcept concept) {
        T metadata = plugin.getMetadata().asStandard();
        return doCreate(metadata);
    }

    public abstract PluginHandler doCreate(T metadata);
}
