package com.github.linyuzai.plugin.core.read;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.util.Properties;


@Getter
@RequiredArgsConstructor
public class PropertiesMetadataReader implements MetadataReader {

    private final Properties properties;

    @Override
    public Object read(Object name) {
        return properties.get(name);
    }
}
