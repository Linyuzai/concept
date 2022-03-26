package com.github.linyuzai.plugin.core.extract;

import com.github.linyuzai.plugin.core.convert.PluginConvertor;
import com.github.linyuzai.plugin.core.format.*;
import com.github.linyuzai.plugin.core.match.PluginMatcher;
import com.github.linyuzai.plugin.core.util.TypeMetadata;

import java.lang.annotation.Annotation;
import java.lang.reflect.Type;

public abstract class TypeMetadataPluginExtractor<T> extends AbstractPluginExtractor<T> {

    @Override
    public PluginMatcher getMatcher(Type type, Annotation[] annotations) {
        TypeMetadata metadata = getAvailableTypeMetadata(type);
        if (metadata == null) {
            return null;
        }
        return getMatcher(metadata, annotations);
    }

    @Override
    public PluginConvertor getConvertor(Type type, Annotation[] annotations) {
        TypeMetadata metadata = getAvailableTypeMetadata(type);
        if (metadata == null) {
            return null;
        }
        return getConvertor(metadata, annotations);
    }

    @Override
    public PluginFormatter getFormatter(Type type, Annotation[] annotations) {
        TypeMetadata metadata = getAvailableTypeMetadata(type);
        if (metadata == null) {
            return null;
        }
        return getFormatter(metadata, annotations);
    }

    public TypeMetadata getAvailableTypeMetadata(Type type) {
        TypeMetadata metadata = createTypeMetadata(type);
        if (metadata == null) {
            return null;
        }
        if (metadata.getTargetClass() == null) {
            return null;
        }
        return metadata;
    }

    public TypeMetadata createTypeMetadata(Type type) {
        return TypeMetadata.create(type);
    }

    public abstract PluginMatcher getMatcher(TypeMetadata metadata, Annotation[] annotations);

    public PluginConvertor getConvertor(TypeMetadata metadata, Annotation[] annotations) {
        return null;
    }

    public PluginFormatter getFormatter(TypeMetadata metadata, Annotation[] annotations) {
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
