package com.github.linyuzai.plugin.core.factory;

import com.github.linyuzai.plugin.core.concept.Plugin;
import com.github.linyuzai.plugin.core.concept.PluginConcept;

public interface PluginFactory {

    boolean support(Object o, PluginConcept concept);

    Plugin create(Object o, PluginConcept concept);
}
