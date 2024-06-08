package com.github.linyuzai.plugin.core.factory;

import com.github.linyuzai.plugin.core.concept.Plugin;
import com.github.linyuzai.plugin.core.context.PluginContext;

public abstract class SubPluginFactory implements PluginFactory {

    @Override
    public Plugin create(Object o, PluginContext context) {
        if (o instanceof Plugin.Entry) {
            return doCreate((Plugin.Entry) o, context);
        }
        return null;
    }

    public abstract Plugin doCreate(Plugin.Entry entry, PluginContext context);
}
