package com.github.linyuzai.plugin.core.factory;

import com.github.linyuzai.plugin.core.concept.Plugin;
import com.github.linyuzai.plugin.core.concept.PluginConcept;

public class DefaultPluginFactory implements PluginFactory {

    @Override
    public boolean support(Object o, PluginConcept concept) {
        return o instanceof Plugin;
    }

    @Override
    public Plugin create(Object o, PluginConcept concept) {
        return (Plugin) o;
    }
}
