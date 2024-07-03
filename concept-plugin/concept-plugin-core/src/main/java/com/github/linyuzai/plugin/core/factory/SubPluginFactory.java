package com.github.linyuzai.plugin.core.factory;

import com.github.linyuzai.plugin.core.concept.Plugin;
import com.github.linyuzai.plugin.core.context.PluginContext;
import com.github.linyuzai.plugin.core.metadata.EmptyMetadata;

public abstract class SubPluginFactory implements PluginFactory {

    private final EmptyMetadata metadata = new EmptyMetadata();

    @Override
    public Plugin create(Object o, PluginContext context) {
        if (o instanceof Plugin.Entry) {
            Plugin plugin = doCreate((Plugin.Entry) o, context);
            if (plugin != null) {
                plugin.setMetadata(metadata);
            }
            return plugin;
        }
        return null;
    }

    public abstract Plugin doCreate(Plugin.Entry entry, PluginContext context);

}
