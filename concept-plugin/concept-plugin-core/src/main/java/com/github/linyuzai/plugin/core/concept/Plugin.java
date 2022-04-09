package com.github.linyuzai.plugin.core.concept;

/**
 * 插件抽象
 */
public interface Plugin {

    String PREFIX = "CONCEPT_PLUGIN@";

    String PATH_NAME = PREFIX + "PATH_NAME";

    String BYTE_ARRAY = PREFIX + "BYTE_ARRAY";

    String PROPERTIES_NAME = PREFIX + "PROPERTIES_NAME";

    String PROPERTIES = PREFIX + "PROPERTIES";

    PluginConcept getPluginConcept();

    Object getId();

    /**
     * 准备
     */
    void prepare();

    /**
     * 释放
     */
    void release();
}
