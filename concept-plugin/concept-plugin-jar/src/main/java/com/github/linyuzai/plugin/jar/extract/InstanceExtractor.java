package com.github.linyuzai.plugin.jar.extract;

import com.github.linyuzai.plugin.core.extract.TypeMetadataPluginExtractor;
import com.github.linyuzai.plugin.core.match.PluginMatcher;
import com.github.linyuzai.plugin.core.util.ReflectionUtils;
import com.github.linyuzai.plugin.core.util.TypeMetadata;
import com.github.linyuzai.plugin.jar.match.*;

import java.lang.annotation.Annotation;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.lang.reflect.WildcardType;

public abstract class InstanceExtractor<T> extends TypeMetadataPluginExtractor<T> {

    @Override
    public PluginMatcher getMatcher(TypeMetadata metadata, Type type, Annotation[] annotations) {
        Type target = metadata.getType();
        Class<?> targetClass = getTargetClass(target);
        if (targetClass == null) {
            return null;
        }
        if (metadata.isMap()) {
            return new InstanceMapMatcher(metadata.getMapClass(), targetClass, annotations);
        } else if (metadata.isList()) {
            return new InstanceListMatcher(metadata.getListClass(), targetClass, annotations);
        } else if (metadata.isSet()) {
            return new InstanceSetMatcher(metadata.getSetClass(), targetClass, annotations);
        } else if (metadata.isCollection()) {
            return new InstanceListMatcher(metadata.getCollectionClass(), targetClass, annotations);
        } else if (metadata.isArray()) {
            return new InstanceArrayMatcher(targetClass, annotations);
        } else {
            return new InstanceObjectMatcher(targetClass, annotations);
        }
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
}
