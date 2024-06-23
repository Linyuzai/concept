package com.github.linyuzai.plugin.core.concept;

import com.github.linyuzai.plugin.core.context.PluginContext;
import com.github.linyuzai.plugin.core.read.PluginReader;

import java.io.IOException;
import java.io.InputStream;
import java.util.Set;

/**
 * 插件抽象
 */
public interface Plugin {

    Object getId();

    Metadata getMetadata();

    void setMetadata(Metadata metadata);

    PluginConcept getConcept();

    void setConcept(PluginConcept concept);

    void addReader(PluginReader reader);

    void removeReader(PluginReader reader);

    void addDestroyListener(DestroyListener listener);

    void removeDestroyListener(DestroyListener listener);

    <T> T read(Class<T> readable, Object key);

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

        interface PropertyKey {

            String PREFIX = "concept.plugin.";

            String NAME = PREFIX + "name";

            String DEPENDENCY_NAMES = PREFIX + "dependency.names";
        }

        String get(String key);

        String get(String key, String defaultValue);

        Set<String> keys();

        <T> T bind(String key, Class<T> type);

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

    interface DestroyListener {

        void onDestroy(Plugin plugin);
    }
}
