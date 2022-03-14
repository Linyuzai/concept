package com.github.linyuzai.plugin.core.concept;

public interface Plugin {

    String PREFIX = "CONCEPT_PLUGIN@";

    String PROPERTIES_NAMES = PREFIX + "PROPERTIES_NAMES";

    String PROPERTIES = PREFIX + "PROPERTIES";

    PluginConcept getPluginConcept();

    void initialize();

    void destroy();
}
