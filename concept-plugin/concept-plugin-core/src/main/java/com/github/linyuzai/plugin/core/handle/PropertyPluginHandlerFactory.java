package com.github.linyuzai.plugin.core.handle;

import com.github.linyuzai.plugin.core.concept.Plugin;
import com.github.linyuzai.plugin.core.context.PluginContext;

public abstract class PropertyPluginHandlerFactory implements PluginHandlerFactory {

    @Override
    public PluginHandler create(PluginContext context) {
        Plugin.Metadata metadata = context.getPlugin().getMetadata();
        String value = metadata.get(getPropertyName());
        if (value == null) {
            return null;
        }
        String[] patterns = value.split(",");
        if (patterns.length == 0) {
            return null;
        }
        return doCreate(patterns);
    }

    public abstract String getPropertyName();

    public abstract PluginHandler doCreate(String[] patterns);
}
