package com.github.linyuzai.plugin.jar.handle.filter;

import com.github.linyuzai.plugin.core.handle.PluginHandler;
import com.github.linyuzai.plugin.core.handle.StringArrayPropertyPluginHandlerFactory;
import com.github.linyuzai.plugin.core.metadata.property.MetadataProperty;
import com.github.linyuzai.plugin.jar.concept.JarPlugin;

public class ClassNameFilterFactory extends StringArrayPropertyPluginHandlerFactory {

    @Override
    public MetadataProperty<String[]> getProperty() {
        return JarPlugin.MetadataProperties.FILTER_CLASS_PATTERNS;
    }

    @Override
    public PluginHandler doCreate(String[] patterns) {
        return new ClassNameFilter(patterns);
    }
}
