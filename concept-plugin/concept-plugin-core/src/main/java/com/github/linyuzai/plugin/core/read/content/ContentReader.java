package com.github.linyuzai.plugin.core.read.content;

import com.github.linyuzai.plugin.core.context.PluginContext;
import com.github.linyuzai.plugin.core.read.PluginReader;

public interface ContentReader extends PluginReader {

    @Override
    PluginContent read(Object key, PluginContext context);

    @Override
    default boolean support(Class<?> readable) {
        return readable == PluginContent.class;
    }
}
