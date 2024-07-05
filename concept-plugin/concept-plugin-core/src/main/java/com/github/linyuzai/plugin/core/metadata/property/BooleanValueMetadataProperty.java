package com.github.linyuzai.plugin.core.metadata.property;

@Deprecated
public class BooleanValueMetadataProperty extends AbstractMetadataProperty<Boolean> {

    public BooleanValueMetadataProperty(String name) {
        super(name, PREFIX);
    }

    public BooleanValueMetadataProperty(String name, MetadataProperty<?> prefix) {
        super(name, prefix);
    }

    @Override
    public Boolean getValue(String value) {
        return Boolean.valueOf(value);
    }
}
