package com.github.linyuzai.plugin.core.handle;

import com.github.linyuzai.plugin.core.metadata.PluginMetadata;
import com.github.linyuzai.plugin.core.context.PluginContext;
import com.github.linyuzai.plugin.core.metadata.property.MetadataProperty;

public abstract class StringArrayPropertyPluginHandlerFactory implements PluginHandlerFactory {

    @Override
    public PluginHandler create(PluginContext context) {
        PluginMetadata metadata = context.getPlugin().getMetadata();
        String[] array = metadata.property(getProperty());
        if (array.length == 0) {
            return null;
        }
        return doCreate(array);
    }

    public abstract MetadataProperty<String[]> getProperty();

    public abstract PluginHandler doCreate(String[] array);
}
