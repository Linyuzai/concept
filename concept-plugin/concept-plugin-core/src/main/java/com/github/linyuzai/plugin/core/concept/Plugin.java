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

    void initialize();

    void destroy();

    /**
     * 准备
     */
    void prepare(PluginContext context);

    /**
     * 释放
     */
    void release(PluginContext context);

    interface Metadata {

        String KEY_NAME = "concept.plugin.name";

        String KEY_DEPENDENCIES = "concept.plugin.dependencies";

        Metadata EMPTY = new Metadata() {
            @Override
            public String get(String key) {
                return null;
            }

            @Override
            public String get(String key, String defaultValue) {
                return defaultValue;
            }

            @Override
            public boolean isEmpty() {
                return true;
            }
        };

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
