package com.github.linyuzai.plugin.core.util;

import com.github.linyuzai.plugin.core.exception.PluginException;
import lombok.Data;

import java.lang.reflect.*;
import java.util.*;

@Data
public class TypeMetadata {

    private Class<?> mapClass;

    private Class<?> listClass;

    private Class<?> setClass;

    private Class<?> collectionClass;

    private Class<?> arrayClass;

    private Type type;

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

    public static TypeMetadata from(Type type) {
        if (type instanceof Class) {
            Class<?> clazz = (Class<?>) type;
            return create(clazz, Object.class);
        } else if (type instanceof ParameterizedType) {
            Type rawType = ((ParameterizedType) type).getRawType();
            Type[] actualTypeArguments = ((ParameterizedType) type).getActualTypeArguments();
            if (rawType instanceof Class) {
                Class<?> clazz = (Class<?>) rawType;
                TypeMetadata metadata = create(clazz, actualTypeArguments[0]);
                if (metadata.isMap()) {
                    Type actualTypeArgument0 = actualTypeArguments[0];
                    if (actualTypeArgument0 instanceof Class &&
                            ((Class<?>) actualTypeArgument0).isAssignableFrom(String.class)) {
                        metadata.type = actualTypeArguments[1];
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
                return from(upperBounds[0]);
            }
            //TODO ? super xxx 好像没有必要
        } else if (type instanceof GenericArrayType) {
            Type componentType = ((GenericArrayType) type).getGenericComponentType();
            TypeMetadata metadata = new TypeMetadata();
            metadata.arrayClass = List.class;
            metadata.type = componentType;
            return metadata;
        }
        return null;
    }

    public static TypeMetadata create(Class<?> clazz, Type type) {
        TypeMetadata metadata = new TypeMetadata();
        if (Map.class.isAssignableFrom(clazz)) {
            metadata.mapClass = clazz;
            metadata.type = type;
        } else if (List.class.isAssignableFrom(clazz)) {
            metadata.listClass = clazz;
            metadata.type = type;
        } else if (Set.class.isAssignableFrom(clazz)) {
            metadata.setClass = clazz;
            metadata.type = type;
        } else if (Collection.class.isAssignableFrom(clazz)) {
            metadata.collectionClass = clazz;
            metadata.type = type;
        } else if (clazz.isArray()) {
            metadata.arrayClass = List.class;
            metadata.type = clazz.getComponentType();
        } else {
            metadata.type = clazz;
        }
        return metadata;
    }
}
