package com.github.linyuzai.plugin.core.metadata;

import com.github.linyuzai.plugin.core.concept.Plugin;
import com.github.linyuzai.plugin.core.concept.PluginDefinition;
import com.github.linyuzai.plugin.core.context.PluginContext;

public class SubPluginMetadataFactory implements PluginMetadataFactory {

    @Override
    public PluginMetadata create(PluginDefinition definition, PluginContext context) {
        if (definition instanceof Plugin.Entry) {
            return EmptyMetadata.STANDARD;
        }
        return null;
    }
}
