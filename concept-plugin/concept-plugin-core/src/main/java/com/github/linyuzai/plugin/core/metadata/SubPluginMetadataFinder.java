package com.github.linyuzai.plugin.core.metadata;

import com.github.linyuzai.plugin.core.concept.Plugin;
import com.github.linyuzai.plugin.core.context.PluginContext;

public class SubPluginMetadataFinder implements PluginMetadataFinder {

    @Override
    public PluginMetadata find(Object source, PluginContext context) {
        if (source instanceof Plugin.Entry) {
            return EmptyMetadata.STANDARD;
        }
        return null;
    }
}
