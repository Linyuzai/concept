package com.github.linyuzai.plugin.core.concept;

import com.github.linyuzai.plugin.core.context.PluginContext;
import com.github.linyuzai.plugin.core.metadata.PluginMetadata;
import com.github.linyuzai.plugin.core.metadata.property.MetadataProperty;
import com.github.linyuzai.plugin.core.metadata.property.PrefixMetadataProperty;
import com.github.linyuzai.plugin.core.metadata.property.StringArrayValueMetadataProperty;
import com.github.linyuzai.plugin.core.metadata.property.StringValueMetadataProperty;
import com.github.linyuzai.plugin.core.read.PluginReader;

import java.io.IOException;
import java.io.InputStream;

/**
 * 插件抽象
 */
public interface Plugin {

    Object getId();

    PluginMetadata getMetadata();

    void setMetadata(PluginMetadata metadata);

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

    interface MetadataProperties {

        MetadataProperty<String> NAME = new StringValueMetadataProperty("name");

        MetadataProperty<?> DEPENDENCY = new PrefixMetadataProperty("dependency");

        MetadataProperty<String[]> DEPENDENCY_NAMES = new StringArrayValueMetadataProperty("names", DEPENDENCY);

        MetadataProperty<?> FILTER = new PrefixMetadataProperty("filter");

        MetadataProperty<?> FILTER_ENTRY = new PrefixMetadataProperty("entry", FILTER);

        MetadataProperty<String[]> FILTER_ENTRY_PATTERNS = new StringArrayValueMetadataProperty("patterns", FILTER_ENTRY);
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
