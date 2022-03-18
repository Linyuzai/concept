package com.github.linyuzai.plugin.core.concept;

public interface Plugin {

    String PREFIX = "CONCEPT_PLUGIN@";

    String PATH_NAME = PREFIX + "PATH_NAME";

    String BYTE_ARRAY = PREFIX + "BYTE_ARRAY";

    String PROPERTIES_NAME = PREFIX + "PROPERTIES_NAME";

    String PROPERTIES = PREFIX + "PROPERTIES";

    PluginConcept getPluginConcept();

    void initialize();

    void destroy();
}
