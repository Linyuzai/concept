package com.github.linyuzai.plugin.core.metadata;

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
    public <T> T standard() {
        return null;
    }

    @Override
    public boolean isEmpty() {
        return true;
    }
}
