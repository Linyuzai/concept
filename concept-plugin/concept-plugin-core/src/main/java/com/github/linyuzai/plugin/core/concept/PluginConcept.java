package com.github.linyuzai.plugin.core.concept;

public interface PluginConcept {

    Plugin add(Object o);

    Plugin load(Object o);

    Plugin load(String id);

    Plugin unload(String id);

    Plugin remove(String id);

    Plugin find(String id);

    boolean isExisted(String id);

    boolean isLoaded(String id);
}
