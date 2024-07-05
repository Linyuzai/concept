package com.github.linyuzai.plugin.core.metadata;

import com.github.linyuzai.plugin.core.concept.Plugin;

import java.util.Collections;
import java.util.Set;

public class EmptyMetadata implements PluginMetadata {

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
    public <T extends Plugin.StandardMetadata> T standard() {
        return null;
    }

    @Override
    public void set(String name, String value) {

    }
}
