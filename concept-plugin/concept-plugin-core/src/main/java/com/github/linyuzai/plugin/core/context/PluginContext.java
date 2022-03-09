package com.github.linyuzai.plugin.core.context;

import com.github.linyuzai.plugin.core.concept.Plugin;

public interface PluginContext {

    <P extends Plugin> P getPlugin();

    <T> T get(Object key);

    void set(Object key, Object value);

    boolean contains(Object key);

    PluginContext duplicate();
}
