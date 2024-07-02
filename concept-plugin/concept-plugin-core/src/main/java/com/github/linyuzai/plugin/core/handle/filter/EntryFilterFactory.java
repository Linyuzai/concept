package com.github.linyuzai.plugin.core.handle.filter;

import com.github.linyuzai.plugin.core.concept.Plugin;
import com.github.linyuzai.plugin.core.handle.PluginHandler;
import com.github.linyuzai.plugin.core.handle.StringArrayPropertyPluginHandlerFactory;
import com.github.linyuzai.plugin.core.metadata.property.MetadataProperty;

public class EntryFilterFactory extends StringArrayPropertyPluginHandlerFactory {

    @Override
    public MetadataProperty<String[]> getProperty() {
        return Plugin.MetadataProperties.FILTER_ENTRY_PATTERNS;
    }

    @Override
    public PluginHandler doCreate(String[] patterns) {
        return new EntryFilter(patterns);
    }
}
