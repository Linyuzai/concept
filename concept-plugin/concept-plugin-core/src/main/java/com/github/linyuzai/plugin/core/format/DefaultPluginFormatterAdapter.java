package com.github.linyuzai.plugin.core.format;

import com.github.linyuzai.plugin.core.util.TypeMetadata;

public class DefaultPluginFormatterAdapter implements PluginFormatterAdapter {

    @Override
    public PluginFormatter adapt(TypeMetadata metadata) {
        //暂时枚举
        if (metadata.isMap()) {
            return new MapToMapFormatter(metadata.getMapClass());
        } else if (metadata.isList()) {
            return new MapToListFormatter(metadata.getListClass());
        } else if (metadata.isSet()) {
            return new MapToSetFormatter(metadata.getSetClass());
        } else if (metadata.isCollection()) {
            return new MapToListFormatter(metadata.getCollectionClass());
        } else if (metadata.isArray()) {
            return new MapToArrayFormatter(metadata.getArrayClass());
        } else {
            return new MapToObjectFormatter();
        }
    }
}
