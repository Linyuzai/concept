package com.github.linyuzai.plugin.core.factory;

import com.github.linyuzai.plugin.core.concept.Plugin;

public interface PluginFactory {

    boolean support(Object o);

    Plugin create(Object o);
}
