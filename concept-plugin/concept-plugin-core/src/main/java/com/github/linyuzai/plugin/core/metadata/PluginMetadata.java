package com.github.linyuzai.plugin.core.metadata;

import com.github.linyuzai.plugin.core.metadata.property.MetadataProperty;

import java.util.Set;

public interface PluginMetadata {

    String NAME = "plugin.properties";

    default <T> T property(MetadataProperty<T> mp) {
        String value = get(mp.getName());
        return mp.getValue(value);
    }

    default <T> T property(MetadataProperty<T> mp, String defaultValue) {
        String value = get(mp.getName(), defaultValue);
        return mp.getValue(value);
    }

    String get(String name);

    String get(String name, String defaultValue);

    Set<String> names();

    <T> T bind(String name, Class<T> type);

    boolean isEmpty();
}
