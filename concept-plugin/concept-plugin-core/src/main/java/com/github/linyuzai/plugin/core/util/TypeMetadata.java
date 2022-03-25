package com.github.linyuzai.plugin.core.util;

import com.github.linyuzai.plugin.core.exception.PluginException;
import lombok.Data;

import java.lang.reflect.GenericArrayType;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.lang.reflect.WildcardType;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Set;

@Data
public class TypeMetadata {

    private Class<?> mapClass;

    private Class<?> listClass;

    private Class<?> setClass;

    private Class<?> collectionClass;

    private Class<?> arrayClass;

    private Type targetType;

    private Class<?> targetClass;

    public boolean isMap() {
        return mapClass != null;
    }

    public boolean isList() {
        return listClass != null;
    }

    public boolean isSet() {
        return setClass != null;
    }

    public boolean isCollection() {
        return collectionClass != null;
    }

    public boolean isArray() {
        return arrayClass != null;
    }

    public boolean isObject() {
        return mapClass == null && listClass == null && setClass == null
                && collectionClass == null && arrayClass == null;
    }

    public static TypeMetadata create(Type type, boolean isClass) {
        if (type instanceof Class) {
            Class<?> clazz = (Class<?>) type;
            return create0(clazz, Object.class, isClass);
        } else if (type instanceof ParameterizedType) {
            Type rawType = ((ParameterizedType) type).getRawType();
            Type[] actualTypeArguments = ((ParameterizedType) type).getActualTypeArguments();
            if (rawType instanceof Class) {
                Class<?> clazz = (Class<?>) rawType;
                TypeMetadata metadata = create0(clazz, actualTypeArguments[0], isClass);
                if (metadata.isMap()) {
                    Type actualTypeArgument0 = actualTypeArguments[0];
                    if (actualTypeArgument0 instanceof Class &&
                            ((Class<?>) actualTypeArgument0).isAssignableFrom(String.class)) {
                        metadata.targetType = actualTypeArguments[1];
                    } else {
                        throw new PluginException("Map key must be String");
                    }
                }
                return metadata;
            }
        } else if (type instanceof WildcardType) {
            WildcardType wildcardType = (WildcardType) type;
            Type[] upperBounds = wildcardType.getUpperBounds();
            if (upperBounds.length > 0) {
                return create(upperBounds[0], isClass);
            }
            //TODO ? super xxx 好像没有必要
        } else if (type instanceof GenericArrayType) {
            Type componentType = ((GenericArrayType) type).getGenericComponentType();
            TypeMetadata metadata = new TypeMetadata();
            metadata.arrayClass = List.class;
            metadata.targetType = componentType;
            metadata.targetClass = getTargetClass(type, isClass);
            return metadata;
        }
        return null;
    }

    private static TypeMetadata create0(Class<?> clazz, Type type, boolean isClass) {
        TypeMetadata metadata = new TypeMetadata();
        if (Map.class.isAssignableFrom(clazz)) {
            metadata.mapClass = clazz;
            metadata.targetType = type;
        } else if (List.class.isAssignableFrom(clazz)) {
            metadata.listClass = clazz;
            metadata.targetType = type;
        } else if (Set.class.isAssignableFrom(clazz)) {
            metadata.setClass = clazz;
            metadata.targetType = type;
        } else if (Collection.class.isAssignableFrom(clazz)) {
            metadata.collectionClass = clazz;
            metadata.targetType = type;
        } else if (clazz.isArray()) {
            metadata.arrayClass = List.class;
            metadata.targetType = clazz.getComponentType();
        } else {
            metadata.targetType = clazz;
        }
        metadata.targetClass = getTargetClass(type, isClass);
        return metadata;
    }

    public static Class<?> getTargetClass(Type type, boolean isClass) {
        if (isClass) {
            if (type instanceof Class) {
                return (Class<?>) type;
            } else if (type instanceof ParameterizedType) {
                Type rawType = ((ParameterizedType) type).getRawType();
                if (rawType instanceof Class) {
                    if (Class.class.isAssignableFrom((Class<?>) rawType)) {
                        Type[] arguments = ((ParameterizedType) type).getActualTypeArguments();
                        return ReflectionUtils.toClass(arguments[0]);
                    }
                }
            } else if (type instanceof WildcardType) {
                Type[] upperBounds = ((WildcardType) type).getUpperBounds();
                if (upperBounds.length > 0) {
                    Type upperBound = upperBounds[0];
                    if (upperBound instanceof Class) {
                        if (Class.class.isAssignableFrom((Class<?>) upperBound)) {
                            return Object.class;
                        }
                    } else if (upperBound instanceof ParameterizedType) {
                        return getTargetClass(upperBound, true);
                    }
                }
            }
            return null;
        } else {
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
}
