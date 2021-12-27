package com.github.linyuzai.download.core.source.reflect.conversion;

import com.github.linyuzai.download.core.exception.DownloadException;

import java.lang.reflect.*;

public interface ValueConvertor<F, T> {

    T convert(F value);

    default boolean support(Object value, Class<?> type) {
        ParameterizedType pt = find(getClass());
        if (pt == null) {
            throw new DownloadException("Cannot happen ???");
        }
        Type[] types = pt.getActualTypeArguments();
        Class<?> fromClass = toClass(types[0]);
        if (fromClass == null) {
            return false;
        }
        if (!fromClass.isInstance(value)) {
            return false;
        }
        Class<?> toClass = toClass(types[1]);
        if (toClass == null) {
            return false;
        }
        if (!toClass.isAssignableFrom(type)) {
            return false;
        }
        return true;
    }

    static Class<?> toClass(Type type) {
        if (type instanceof Class) {
            return (Class<?>) type;
        }
        if (type instanceof ParameterizedType) {
            return toClass(((ParameterizedType) type).getRawType());
        }
        if (type instanceof WildcardType || type instanceof TypeVariable || type instanceof GenericArrayType) {
            //能拿到泛型，但是没有具体类型
            //需要其他泛型类配合输出
        }
        return null;
    }

    static ParameterizedType find(Class<?> clazz) {
        if (clazz == null) {
            return null;
        }
        Type[] interfaces = clazz.getGenericInterfaces();
        for (Type i : interfaces) {
            if (i instanceof ParameterizedType) {
                ParameterizedType pt = (ParameterizedType) i;
                if (pt.getRawType() == ValueConvertor.class) {
                    return pt;
                }
            }
        }
        return find(clazz.getSuperclass());
    }
}
