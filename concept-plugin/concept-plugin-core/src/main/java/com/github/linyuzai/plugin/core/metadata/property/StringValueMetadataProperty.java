package com.github.linyuzai.plugin.core.metadata.property;

public class StringValueMetadataProperty extends AbstractMetadataProperty<String> {

    public StringValueMetadataProperty(String name) {
        super(name, PREFIX);
    }

    public StringValueMetadataProperty(String name, MetadataProperty<?> prefix) {
        super(name, prefix);
    }

    @Override
    public String getValue(String value) {
        return value;
    }
}
