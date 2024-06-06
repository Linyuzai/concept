package com.github.linyuzai.plugin.core.read.metadata;

import com.github.linyuzai.plugin.core.context.PluginContext;
import com.github.linyuzai.plugin.core.read.PluginReader;

public interface MetadataReader extends PluginReader {

    @Override
    PluginMetadata read(Object key, PluginContext context);

    @Override
    default boolean support(Class<?> readable) {
        return readable == PluginMetadata.class;
    }
}
