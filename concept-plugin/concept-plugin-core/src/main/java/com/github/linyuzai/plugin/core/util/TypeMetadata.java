package com.github.linyuzai.plugin.core.util;

import com.github.linyuzai.plugin.core.exception.PluginException;
import lombok.Data;
import lombok.SneakyThrows;

import java.lang.reflect.*;
import java.util.*;

@Data
@SuppressWarnings("unchecked")
public class TypeMetadata {

    private Map<String, Object> map;

    private List<Object> list;

    private Set<Object> set;

    private Collection<Object> collection;

    private List<Object> array;

    private Type type;

    public boolean isMap() {
        return map != null;
    }

    public boolean isList() {
        return list != null;
    }

    public boolean isSet() {
        return set != null;
    }

    public boolean isCollection() {
        return collection != null;
    }

    public boolean isArray() {
        return array != null;
    }

    public boolean isObject() {
        return map == null && list == null && set == null && collection == null && array == null;
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
            metadata.array = newList(List.class);
            metadata.type = componentType;
            return metadata;
        }
        return null;
    }

    public static TypeMetadata create(Class<?> clazz, Type type) {
        TypeMetadata metadata = new TypeMetadata();
        if (Map.class.isAssignableFrom(clazz)) {
            metadata.map = newMap(clazz);
            metadata.type = type;
        } else if (List.class.isAssignableFrom(clazz)) {
            metadata.list = newList(clazz);
            metadata.type = type;
        } else if (Set.class.isAssignableFrom(clazz)) {
            metadata.set = newSet(clazz);
            metadata.type = type;
        } else if (Collection.class.isAssignableFrom(clazz)) {
            metadata.collection = newList(clazz);
            metadata.type = type;
        } else if (clazz.isArray()) {
            metadata.array = newList(clazz);
            metadata.type = clazz.getComponentType();
        } else {
            metadata.type = clazz;
        }
        return metadata;
    }

    @SneakyThrows
    public static <E> List<E> newList(Class<?> clazz) {
        int modifiers = clazz.getModifiers();
        if (Modifier.isInterface(modifiers) || Modifier.isAbstract(modifiers)) {
            return new ArrayList<>();
        } else {
            return (List<E>) clazz.newInstance();
        }
    }

    @SneakyThrows
    public static <E> Set<E> newSet(Class<?> clazz) {
        int modifiers = clazz.getModifiers();
        if (Modifier.isInterface(modifiers) || Modifier.isAbstract(modifiers)) {
            return new HashSet<>();
        } else {
            return (Set<E>) clazz.newInstance();
        }
    }

    @SneakyThrows
    public static <E> Map<String, E> newMap(Class<?> clazz) {
        int modifiers = clazz.getModifiers();
        if (Modifier.isInterface(modifiers) || Modifier.isAbstract(modifiers)) {
            return new HashMap<>();
        } else {
            return (Map<String, E>) clazz.newInstance();
        }
    }
}
