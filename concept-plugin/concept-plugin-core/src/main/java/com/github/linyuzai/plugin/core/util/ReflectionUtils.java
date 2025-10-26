package com.github.linyuzai.plugin.core.util;

import lombok.SneakyThrows;

import java.lang.annotation.Annotation;
import java.lang.reflect.*;
import java.util.*;
import java.util.function.BiConsumer;

/**
 * 反射相关工具类
 */
@SuppressWarnings("unchecked")
public class ReflectionUtils {

    /**
     * 获得注解
     */
    public static <A extends Annotation> A findAnnotation(Class<?> clazz, Class<A> annotationType) {
        Class<?> c = clazz;
        while (c != null) {
            A annotation = c.getAnnotation(annotationType);
            if (annotation != null) {
                return annotation;
            }
            c = c.getSuperclass();
        }
        return null;
    }

    /**
     * 将 {@link Type} 转为 {@link Class}
     */
    public static Class<?> toClass(Type type) {
        List<Class<?>> list = new ArrayList<>();
        resolve(type, (cls, types) -> list.add(cls));
        if (list.size() == 1) {
            return list.get(0);
        }
        return null;
    }

    /**
     * 根据 {@link Type} 获得 {@link Class} 和范型
     */
    public static void resolve(Type type, BiConsumer<Class<?>, Type[]> consumer) {
        if (type instanceof Class) {
            Type[] types;
            if (((Class<?>) type).isArray()) {
                types = new Type[]{((Class<?>) type).getComponentType()};
            } else {
                types = new Type[]{};
            }
            consumer.accept((Class<?>) type, types);
        } else if (type instanceof ParameterizedType) {
            Type rawType = ((ParameterizedType) type).getRawType();
            if (rawType instanceof Class) {
                Type[] actualTypeArguments = ((ParameterizedType) type).getActualTypeArguments();
                consumer.accept((Class<?>) rawType, actualTypeArguments);
            }
        } else if (type instanceof WildcardType) {
            Type[] upperBounds = ((WildcardType) type).getUpperBounds();
            if (upperBounds.length == 1) {
                resolve(upperBounds[0], consumer);
            }
            Type[] lowerBounds = ((WildcardType) type).getLowerBounds();
            if (lowerBounds.length == 1) {
                resolve(lowerBounds[0], consumer);
            }
        } else if (type instanceof GenericArrayType) {
            // A<?>[] A<B>[]
            Type componentType = ((GenericArrayType) type).getGenericComponentType();
            resolve(componentType, (componentClass, types) -> {
                Object array = Array.newInstance(componentClass, 0);
                Class<?> arrayClass = array.getClass();
                consumer.accept(arrayClass, new Type[]{componentType});
            });
        }
        /*else if (type instanceof TypeVariable) {

        }*/
    }

    @SneakyThrows
    public static <E> Collection<E> newCollection(Class<?> clazz) {
        return newList(clazz);
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
    public static <K, E> Map<K, E> newMap(Class<?> clazz) {
        int modifiers = clazz.getModifiers();
        if (Modifier.isInterface(modifiers) || Modifier.isAbstract(modifiers)) {
            return new LinkedHashMap<>();
        } else {
            return (Map<K, E>) clazz.newInstance();
        }
    }
}
