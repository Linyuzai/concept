package com.github.linyuzai.plugin.core.metadata.property;

import lombok.Getter;

@Getter
public abstract class AbstractMetadataProperty<T> implements MetadataProperty<T> {

    private final String name;

    public AbstractMetadataProperty(String name, MetadataProperty<?> prefix) {
        if (prefix == null) {
            this.name = name;
        } else {
            this.name = prefix.getName() + "." + name;
        }
    }
}
