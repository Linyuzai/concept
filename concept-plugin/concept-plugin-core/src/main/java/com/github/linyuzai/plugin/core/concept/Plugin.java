package com.github.linyuzai.plugin.core.concept;

import com.github.linyuzai.plugin.core.context.PluginContext;

import java.io.IOException;
import java.io.InputStream;

/**
 * 插件抽象
 */
public interface Plugin {

    Object getId();

    <T> T read(Class<T> readable, Object key);

    Metadata getMetadata();

    void setMetadata(Metadata metadata);

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

    interface Metadata {

        String get(String key);

        String get(String key, String defaultValue);

        boolean isEmpty();
    }

    interface Entry {

        Object getId();

        String getName();

        Plugin getPlugin();

        Content getContent();
    }

    interface Content {

        InputStream getInputStream() throws IOException;
    }
}
