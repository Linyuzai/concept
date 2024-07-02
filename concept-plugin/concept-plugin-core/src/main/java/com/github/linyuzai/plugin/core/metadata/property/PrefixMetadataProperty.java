package com.github.linyuzai.plugin.core.metadata.property;

public class PrefixMetadataProperty extends AbstractMetadataProperty<Object> {

    public PrefixMetadataProperty(String name) {
        super(name, PREFIX);
    }

    public PrefixMetadataProperty(String name, MetadataProperty<?> prefix) {
        super(name, prefix);
    }

    @Override
    public Object getValue(String value) {
        throw new UnsupportedOperationException();
    }
}
