package com.github.linyuzai.plugin.core.metadata.property;

public interface MetadataProperty<T> {

    MetadataProperty<?> PREFIX = new StringValueMetadataProperty("concept.plugin", null);

    String getName();

    T getValue(String value);
}
