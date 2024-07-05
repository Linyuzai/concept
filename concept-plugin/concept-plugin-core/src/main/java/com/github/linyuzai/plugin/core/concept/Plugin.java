package com.github.linyuzai.plugin.core.concept;

import com.github.linyuzai.plugin.core.context.PluginContext;
import com.github.linyuzai.plugin.core.metadata.PluginMetadata;
import com.github.linyuzai.plugin.core.read.PluginReader;
import lombok.Data;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.Collections;
import java.util.Set;

/**
 * 插件抽象
 */
public interface Plugin {

    Object getId();

    Object getSource();

    URL getURL();

    PluginMetadata getMetadata();

    void setMetadata(PluginMetadata metadata);

    PluginConcept getConcept();

    void setConcept(PluginConcept concept);

    void addReader(PluginReader reader);

    void removeReader(PluginReader reader);

    void addDestroyListener(DestroyListener listener);

    void removeDestroyListener(DestroyListener listener);

    <T> T read(Class<T> readable, Object key);

    <T> T read(Class<T> readable, Object key, PluginContext context);

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

    @Data
    class StandardMetadata {

        private String name;

        private HandlerMetadata handler = new HandlerMetadata();

        private DependencyMetadata dependency = new DependencyMetadata();

        //private FilterMetadata filter = new FilterMetadata();

        @Data
        public static class HandlerMetadata {

            private boolean enabled = true;
        }

        @Data
        public static class DependencyMetadata {

            private Set<String> names = Collections.emptySet();
        }

        /*@Data
        public static class FilterMetadata {

            private EntryMetadata entry = new EntryMetadata();

            @Data
            public static class EntryMetadata {

                private Set<String> patterns;
            }
        }*/
    }

    /*interface MetadataProperties {

        MetadataProperty<String> NAME = new StringValueMetadataProperty("name");

        MetadataProperty<?> DEPENDENCY = new PrefixMetadataProperty("dependency");

        MetadataProperty<String[]> DEPENDENCY_NAMES = new StringArrayValueMetadataProperty("names", DEPENDENCY);

        MetadataProperty<?> FILTER = new PrefixMetadataProperty("filter");

        MetadataProperty<?> FILTER_ENTRY = new PrefixMetadataProperty("entry", FILTER);

        MetadataProperty<String[]> FILTER_ENTRY_PATTERNS = new StringArrayValueMetadataProperty("patterns", FILTER_ENTRY);
    }*/

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
