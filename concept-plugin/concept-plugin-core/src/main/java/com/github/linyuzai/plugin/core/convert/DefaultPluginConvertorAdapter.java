package com.github.linyuzai.plugin.core.convert;

import com.github.linyuzai.plugin.core.util.TypeMetadata;

public class DefaultPluginConvertorAdapter implements PluginConvertorAdapter {

    @Override
    public PluginConvertor adapt(TypeMetadata metadata) {
        Class<?> target = metadata.getTargetClass();
        if (metadata.isMap()) {
            return new MapToMapConvertor(metadata.getMapClass());
        } else if (metadata.isList()) {
            return new MapToListConvertor(metadata.getListClass());
        } else if (metadata.isSet()) {
            return new MapToSetConvertor(metadata.getSetClass());
        } else if (metadata.isCollection()) {
            return new MapToListConvertor(metadata.getCollectionClass());
        } else if (metadata.isArray()) {
            return new MapToArrayConvertor(target);
        } else {
            return new MapToObjectConvertor();
        }
    }
}
