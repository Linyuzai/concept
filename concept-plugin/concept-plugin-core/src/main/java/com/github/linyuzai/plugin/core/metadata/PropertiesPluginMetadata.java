package com.github.linyuzai.plugin.core.metadata;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.util.Properties;
import java.util.Set;

@Getter
@RequiredArgsConstructor
public class PropertiesPluginMetadata implements PluginMetadata {

    private final Properties properties;

    @Override
    public String get(String name) {
        return properties.getProperty(name);
    }

    @Override
    public String get(String name, String defaultValue) {
        return properties.getProperty(name, defaultValue);
    }

    @Override
    public Set<String> names() {
        return properties.stringPropertyNames();
    }

    @Override
    public <T> T bind(String name, Class<T> type) {
        throw new UnsupportedOperationException();
    }

    @Override
    public boolean isEmpty() {
        return properties.isEmpty();
    }
}
