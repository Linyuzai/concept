package com.github.linyuzai.plugin.jar.handle.filter;

import com.github.linyuzai.plugin.core.handle.PluginHandler;
import com.github.linyuzai.plugin.core.handle.StandardMetadataPluginHandlerFactory;
import com.github.linyuzai.plugin.jar.concept.JarPlugin;

import java.util.Set;

public class ClassNameFilterFactory extends StandardMetadataPluginHandlerFactory<JarPlugin.StandardMetadata> {

    @Override
    public PluginHandler doCreate(JarPlugin.StandardMetadata metadata) {
        Set<String> patterns = metadata.getFilter().getClassName().getPatterns();
        if (patterns == null || patterns.isEmpty()) {
            return null;
        }
        return new ClassNameFilter(patterns);
    }
}
