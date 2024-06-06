package com.github.linyuzai.plugin.core.factory;

import com.github.linyuzai.plugin.core.concept.Plugin;
import com.github.linyuzai.plugin.core.concept.PluginConcept;
import com.github.linyuzai.plugin.core.concept.PluginEntry;

public abstract class SubPluginFactory implements PluginFactory {

    @Override
    public boolean support(Object o, PluginConcept concept) {
        if (o instanceof PluginEntry) {
            return doSupport((PluginEntry) o, concept);
        } else {
            return false;
        }
    }

    public abstract boolean doSupport(PluginEntry entry, PluginConcept concept);

    @Override
    public Plugin create(Object o, PluginConcept concept) {
        return doCreate((PluginEntry) o, concept);
    }

    public abstract Plugin doCreate(PluginEntry entry, PluginConcept concept);
}
