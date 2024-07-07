package com.github.linyuzai.plugin.core.handle.filter;

import com.github.linyuzai.plugin.core.concept.Plugin;
import com.github.linyuzai.plugin.core.handle.PluginHandler;
import com.github.linyuzai.plugin.core.handle.StandardMetadataPluginHandlerFactory;

@Deprecated
public class EntryFilterFactory extends StandardMetadataPluginHandlerFactory<Plugin.StandardMetadata> {

    @Override
    public PluginHandler doCreate(Plugin.StandardMetadata metadata) {
        /*Set<String> patterns = metadata.getFilter().getEntry().getPatterns();
        if (patterns == null || patterns.isEmpty()) {
            return null;
        }
        return new EntryFilter(patterns);*/
        return null;
    }
}
