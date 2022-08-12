package com.github.linyuzai.event.core.utils;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.lang.reflect.WildcardType;

/**
 * 泛型获取
 *
 * @param <T> 类型
 */
public interface GenericProvider<T> {

    /**
     * 获得泛型
     */
    default Type getGenericType() {
        Class<?> clazz = getClass();
        while (clazz != null) {
            Type type = clazz.getGenericSuperclass();
            if (type instanceof ParameterizedType) {
                if (((ParameterizedType) type).getRawType() == getTarget()) {
                    Type[] types = ((ParameterizedType) type).getActualTypeArguments();
                    if (types.length == 1) {
                        return types[0];
                    }
                }
            }
            clazz = clazz.getSuperclass();
        }
        return null;
    }

    /**
     * 获得泛型对应的 Class
     */
    default Class<T> getGenericClass() {
        return toClass(getGenericType());
    }

    default T adaptGeneric(Object o) {
        Class<T> clazz = getGenericClass();
        if (clazz != null && clazz.isInstance(o)) {
            return clazz.cast(o);
        } else {
            return null;
        }
    }

    /**
     * 指定泛型的 RawType
     */
    Class<?> getTarget();

    /**
     * Type to Class
     */
    @SuppressWarnings("unchecked")
    default Class<T> toClass(Type type) {
        if (type instanceof Class) {
            return (Class<T>) type;
        } else if (type instanceof ParameterizedType) {
            Type rawType = ((ParameterizedType) type).getRawType();
            if (rawType instanceof Class) {
                return (Class<T>) rawType;
            }
            return null;
        } else if (type instanceof WildcardType) {
            Type[] upperBounds = ((WildcardType) type).getUpperBounds();
            if (upperBounds.length > 0) {
                return toClass(upperBounds[0]);
            }
            Type[] lowerBounds = ((WildcardType) type).getLowerBounds();
            if (lowerBounds.length > 0) {
                return toClass(lowerBounds[0]);
            }
            return null;
        }
        return null;
    }
}
