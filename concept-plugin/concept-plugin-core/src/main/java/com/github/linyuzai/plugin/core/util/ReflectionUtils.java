package com.github.linyuzai.plugin.core.util;

import lombok.SneakyThrows;

import java.lang.reflect.Modifier;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.lang.reflect.WildcardType;
import java.util.*;

@SuppressWarnings("unchecked")
public class ReflectionUtils {

    public static Class<?> toClass(Type type) {
        if (type instanceof Class) {
            return (Class<?>) type;
        } else if (type instanceof ParameterizedType) {
            Type rawType = ((ParameterizedType) type).getRawType();
            if (rawType instanceof Class) {
                return (Class<?>) rawType;
            }
            return null;
        } else if (type instanceof WildcardType) {
            Type[] upperBounds = ((WildcardType) type).getUpperBounds();
            if (upperBounds.length > 0) {
                return toClass(upperBounds[0]);
            }
            return null;
        }
        return null;
    }

    @SneakyThrows
    public static <E> Collection<E> newCollection(Class<?> clazz) {
        int modifiers = clazz.getModifiers();
        if (Modifier.isInterface(modifiers) || Modifier.isAbstract(modifiers)) {
            return new ArrayList<>();
        } else {
            return (Collection<E>) clazz.newInstance();
        }
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
            return new LinkedHashSet<>();
        } else {
            return (Set<E>) clazz.newInstance();
        }
    }

    @SneakyThrows
    public static <E> Map<String, E> newMap(Class<?> clazz) {
        int modifiers = clazz.getModifiers();
        if (Modifier.isInterface(modifiers) || Modifier.isAbstract(modifiers)) {
            return new LinkedHashMap<>();
        } else {
            return (Map<String, E>) clazz.newInstance();
        }
    }
}
