package com.github.linyuzai.plugin.core.metadata.property;

@Deprecated
public class StringArrayValueMetadataProperty extends AbstractMetadataProperty<String[]> {

    public StringArrayValueMetadataProperty(String name) {
        super(name, PREFIX);
    }

    public StringArrayValueMetadataProperty(String name, MetadataProperty<?> prefix) {
        super(name, prefix);
    }

    @Override
    public String[] getValue(String value) {
        if (value == null) {
            return new String[0];
        }
        return value.split(",");
    }
}
