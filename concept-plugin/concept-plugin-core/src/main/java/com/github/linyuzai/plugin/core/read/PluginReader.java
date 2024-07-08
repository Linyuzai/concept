package com.github.linyuzai.plugin.core.read;

import com.github.linyuzai.plugin.core.context.PluginContext;

import java.io.Closeable;
import java.io.IOException;

@Deprecated
public interface PluginReader extends Closeable {

    Object read(Object key, PluginContext context);

    boolean support(Class<?> readable);

    @Override
    default void close() throws IOException {

    }
}
