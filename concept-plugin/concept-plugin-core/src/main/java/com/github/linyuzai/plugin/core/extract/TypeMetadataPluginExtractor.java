package com.github.linyuzai.plugin.core.extract;

import com.github.linyuzai.plugin.core.convert.DefaultPluginConvertorAdapter;
import com.github.linyuzai.plugin.core.convert.PluginConvertor;
import com.github.linyuzai.plugin.core.convert.PluginConvertorAdapter;
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
    private PluginConvertorAdapter convertorAdapter = new DefaultPluginConvertorAdapter();

    @Override
    public PluginMatcher getMatcher(Type type, Annotation[] annotations) {
        TypeMetadata metadata = TypeMetadata.from(type);
        if (metadata == null) {
            return null;
        }
        metadata.setTargetClass(getTargetClass(metadata.getTargetType()));
        if (metadata.getTargetClass() == null) {
            return null;
        }
        convertor = getConvertor(metadata);
        return getMatcher(metadata, annotations);
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

    public PluginConvertor getConvertor(TypeMetadata metadata) {
        return convertorAdapter.adapt(metadata);
    }

    public abstract PluginMatcher getMatcher(TypeMetadata metadata, Annotation[] annotations);
}
