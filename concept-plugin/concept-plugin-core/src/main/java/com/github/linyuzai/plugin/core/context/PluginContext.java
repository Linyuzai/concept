package com.github.linyuzai.plugin.core.context;

import com.github.linyuzai.plugin.core.concept.Plugin;
import com.github.linyuzai.plugin.core.concept.PluginConcept;

public interface PluginContext {

    <C extends PluginConcept> C getPluginConcept();

    <P extends Plugin> P getPlugin();

    <T> T get(Object key);

    void set(Object key, Object value);

    boolean contains(Object key);

    void initialize();

    void destroy();
}
