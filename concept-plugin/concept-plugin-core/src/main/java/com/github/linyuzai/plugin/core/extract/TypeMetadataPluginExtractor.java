package com.github.linyuzai.plugin.core.extract;

import com.github.linyuzai.plugin.core.convert.PluginConvertor;
import com.github.linyuzai.plugin.core.format.DefaultPluginFormatterAdapter;
import com.github.linyuzai.plugin.core.format.PluginFormatter;
import com.github.linyuzai.plugin.core.format.PluginFormatterAdapter;
import com.github.linyuzai.plugin.core.match.PluginMatcher;
import com.github.linyuzai.plugin.core.util.ReflectionUtils;
import com.github.linyuzai.plugin.core.util.TypeMetadata;
import lombok.Getter;
import lombok.Setter;

import java.lang.annotation.Annotation;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.lang.reflect.WildcardType;

public abstract class TypeMetadataPluginExtractor<T> extends AbstractPluginExtractor<T> {

    @Getter
    @Setter
    private PluginFormatterAdapter formatterAdapter = new DefaultPluginFormatterAdapter();

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
        TypeMetadata metadata = TypeMetadata.from(type);
        if (metadata == null) {
            return null;
        }
        metadata.setTargetClass(getTargetClass(metadata.getTargetType()));
        if (metadata.getTargetClass() == null) {
            return null;
        }
        return metadata;
    }

    public Class<?> getTargetClass(Type type) {
        if (type instanceof Class) {
            return (Class<?>) type;
        } else if (type instanceof ParameterizedType) {
            Type rawType = ((ParameterizedType) type).getRawType();
            return ReflectionUtils.toClass(rawType);
        } else if (type instanceof WildcardType) {
            Type[] upperBounds = ((WildcardType) type).getUpperBounds();
            if (upperBounds.length > 0) {
                return ReflectionUtils.toClass(upperBounds[0]);
            }
        }
        return null;
    }

    public abstract PluginMatcher getMatcher(TypeMetadata metadata, Annotation[] annotations);

    public PluginConvertor getConvertor(TypeMetadata metadata, Annotation[] annotations) {
        return null;
    }

    public PluginFormatter getFormatter(TypeMetadata metadata, Annotation[] annotations) {
        return formatterAdapter.adapt(metadata);
    }
}
