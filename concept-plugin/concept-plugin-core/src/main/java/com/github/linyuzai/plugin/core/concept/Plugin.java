package com.github.linyuzai.plugin.core.concept;

import com.github.linyuzai.plugin.core.context.PluginContext;
import com.github.linyuzai.plugin.core.read.PluginReader;

import java.util.Collection;
import java.util.Properties;

/**
 * 插件抽象
 */
public interface Plugin {

    String PREFIX = "CONCEPT_PLUGIN@";

    String PATH_NAME = PREFIX + "PATH_NAME";

    String BYTE_ARRAY = PREFIX + "BYTE_ARRAY";

    String PROPERTIES_NAME = PREFIX + "PROPERTIES_NAME";

    String PROPERTIES = PREFIX + "PROPERTIES";

    Object getId();

    void addReaders(PluginReader... readers);

    Collection<PluginReader> getReaders(Class<? extends PluginReader> type);

    <T> T read(Class<? extends PluginReader> type, String name);

    PluginConcept getConcept();

    void setConcept(PluginConcept concept);

    /**
     * 准备
     */
    void prepare(PluginContext context);

    /**
     * 释放
     */
    void release(PluginContext context);
}
