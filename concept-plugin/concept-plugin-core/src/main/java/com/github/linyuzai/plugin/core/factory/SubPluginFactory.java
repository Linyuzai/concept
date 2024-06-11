package com.github.linyuzai.plugin.core.factory;

import com.github.linyuzai.plugin.core.concept.Plugin;
import com.github.linyuzai.plugin.core.context.PluginContext;

public abstract class SubPluginFactory implements PluginFactory {

    @Override
    public Plugin create(Object o, PluginContext context) {
        if (o instanceof Plugin.Entry) {
            Plugin plugin = doCreate((Plugin.Entry) o, context);
            if (plugin != null) {
                plugin.setMetadata(Plugin.Metadata.EMPTY);
            }
            return plugin;
        }
        return null;
    }

    public abstract Plugin doCreate(Plugin.Entry entry, PluginContext context);
}
