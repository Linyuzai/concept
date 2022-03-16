package com.github.linyuzai.plugin.core.concept;

public interface Plugin {

    String PREFIX = "CONCEPT_PLUGIN@";

    String FILE_NAMES = PREFIX + "FILE_NAMES";

    String INPUT_STREAMS = PREFIX + "INPUT_STREAMS";

    String BYTES = PREFIX + "BYTES";

    String PROPERTIES_NAMES = PREFIX + "PROPERTIES_NAMES";

    String PROPERTIES = PREFIX + "PROPERTIES";

    PluginConcept getPluginConcept();

    void initialize();

    void destroy();
}
