package com.github.linyuzai.download.core.source.reflect.conversion;

import com.github.linyuzai.download.core.exception.DownloadException;

import java.lang.reflect.*;

/**
 * 值转换器。
 *
 * @param <Original> 原始类型
 * @param <Target>   目标类型
 */
public interface ValueConvertor<Original, Target> {

    /**
     * 转换。
     *
     * @param value 原始值
     * @return 目标值
     */
    Target convert(Original value);

    /**
     * 转换器是否支持将值转为对应的类型。
     *
     * @param value 需要转换的值
     * @param type  目标类型
     * @return 如果支持则返回 true
     */
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
        if (!type.isAssignableFrom(toClass)) {
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

    /**
     * 找到指定的泛型接口。
     *
     * @param clazz Class
     * @return 泛型接口
     */
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
