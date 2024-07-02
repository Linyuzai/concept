package com.github.linyuzai.plugin.core.factory;

import com.github.linyuzai.plugin.core.concept.Plugin;
import com.github.linyuzai.plugin.core.metadata.PluginMetadata;
import com.github.linyuzai.plugin.core.context.PluginContext;

import java.util.Collections;
import java.util.Set;

public abstract class SubPluginFactory implements PluginFactory {

    private final SubPluginMetadata metadata = new SubPluginMetadata();

    @Override
    public Plugin create(Object o, PluginContext context) {
        if (o instanceof Plugin.Entry) {
            Plugin plugin = doCreate((Plugin.Entry) o, context);
            if (plugin != null) {
                plugin.setMetadata(metadata);
            }
            return plugin;
        }
        return null;
    }

    public abstract Plugin doCreate(Plugin.Entry entry, PluginContext context);

    public static class SubPluginMetadata implements PluginMetadata {

        @Override
        public String get(String name) {
            return null;
        }

        @Override
        public String get(String name, String defaultValue) {
            return defaultValue;
        }

        @Override
        public Set<String> names() {
            return Collections.emptySet();
        }

        @Override
        public <T> T bind(String name, Class<T> type) {
            return null;
        }

        @Override
        public boolean isEmpty() {
            return true;
        }
    }
}
