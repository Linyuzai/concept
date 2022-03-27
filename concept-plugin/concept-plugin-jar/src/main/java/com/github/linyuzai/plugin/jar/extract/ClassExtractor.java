package com.github.linyuzai.plugin.jar.extract;

import com.github.linyuzai.plugin.core.extract.TypeMetadataPluginExtractor;
import com.github.linyuzai.plugin.core.match.PluginMatcher;
import com.github.linyuzai.plugin.core.util.ReflectionUtils;
import com.github.linyuzai.plugin.core.util.TypeMetadata;
import com.github.linyuzai.plugin.jar.match.ClassMatcher;

import java.lang.annotation.Annotation;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.lang.reflect.WildcardType;

public abstract class ClassExtractor<T> extends TypeMetadataPluginExtractor<T> {

    @Override
    public PluginMatcher getMatcher(TypeMetadata metadata, Annotation[] annotations) {
        Class<?> target = metadata.getTargetClass();
        return new ClassMatcher(target, annotations);
    }

    @Override
    public TypeMetadata createTypeMetadata(Type type) {
        TypeMetadata metadata = TypeMetadata.create(type);
        if (metadata == null) {
            return null;
        }
        metadata.setTargetClass(getTargetClass(metadata.getTargetType()));
        if (metadata.isArray()) {
            metadata.setArrayClass(Class.class);
        }
        return metadata;
    }

    public static Class<?> getTargetClass(Type type) {
        if (type instanceof Class) {
            if (type == Class.class) {
                return Object.class;
            }
        } else if (type instanceof ParameterizedType) {
            Type rawType = ((ParameterizedType) type).getRawType();
            if (rawType instanceof Class) {
                if (rawType == Class.class) {
                    Type[] arguments = ((ParameterizedType) type).getActualTypeArguments();
                    return ReflectionUtils.toClass(arguments[0]);
                }
            }
        } else if (type instanceof WildcardType) {
            Type[] upperBounds = ((WildcardType) type).getUpperBounds();
            if (upperBounds.length > 0) {
                Type upperBound = upperBounds[0];
                if (upperBound instanceof Class || upperBound instanceof ParameterizedType) {
                    return getTargetClass(upperBound);
                }
            }
        }
        return null;
    }
}
