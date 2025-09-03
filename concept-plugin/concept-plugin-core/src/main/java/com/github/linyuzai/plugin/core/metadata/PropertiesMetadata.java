package com.github.linyuzai.plugin.core.metadata;

import com.github.linyuzai.plugin.core.concept.Plugin;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.util.Properties;
import java.util.Set;

/**
 * 基于 {@link Properties} 的插件配置
 */
@Getter
@RequiredArgsConstructor
public class PropertiesMetadata implements PluginMetadata {

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
    public <T extends Plugin.StandardMetadata> T asStandard() {
        throw new UnsupportedOperationException();
    }

    @Override
    public void set(String name, String value) {
        properties.setProperty(name, value);
    }

    @Override
    public void refresh() {

    }
}
