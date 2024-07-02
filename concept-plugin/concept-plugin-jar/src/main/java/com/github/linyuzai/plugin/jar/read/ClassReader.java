package com.github.linyuzai.plugin.jar.read;

import com.github.linyuzai.plugin.core.concept.Plugin;
import com.github.linyuzai.plugin.core.context.PluginContext;
import com.github.linyuzai.plugin.core.read.PluginReader;

public interface ClassReader extends PluginReader {

    @Override
    Class<?> read(Object key, PluginContext context);

    @Override
    default boolean support(Class<?> readable) {
        return readable == Class.class;
    }

    Plugin getPlugin();
}
