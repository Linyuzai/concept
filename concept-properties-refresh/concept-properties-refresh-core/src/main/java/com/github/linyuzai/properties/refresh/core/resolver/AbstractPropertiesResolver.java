package com.github.linyuzai.properties.refresh.core.resolver;

import java.lang.reflect.*;
import java.util.List;
import java.util.Map;
import java.util.Set;

public abstract class AbstractPropertiesResolver implements PropertiesResolver {

    @Override
    public Object resolve(String key, Type type) {
        if (type instanceof ParameterizedType) {
            //如果是普通的泛型
            Type[] arguments = ((ParameterizedType) type).getActualTypeArguments();
            Type rawType = ((ParameterizedType) type).getRawType();
            if (rawType instanceof Class) {
                //主Class
                Class<?> clazz = (Class<?>) rawType;
                if (List.class.isAssignableFrom(clazz)) {
                    return getList(key, getParameterizedClass(arguments[0]));
                } else if (Set.class.isAssignableFrom(clazz)) {
                    return getSet(key, getParameterizedClass(arguments[0]));
                } else if (Map.class.isAssignableFrom(clazz)) {
                    return getMap(key, getParameterizedClass(arguments[1]));
                } else {
                    //TODO 自定义的泛型类？
                    return bind(key, clazz);
                }
            } else {
                //TODO 没遇到过
                throw new UnsupportedOperationException();
            }
        } else if (type instanceof GenericArrayType || type instanceof AnnotatedArrayType) {
            //TODO 不支持处理泛型数组和带注解的数组
            throw new UnsupportedOperationException();
        } else {
            //如果是Class
            Class<?> clazz = (Class<?>) type;
            if (List.class.isAssignableFrom(clazz)) {
                return getList(key);
            } else if (Set.class.isAssignableFrom(clazz)) {
                return getSet(key);
            } else if (Map.class.isAssignableFrom(clazz)) {
                return getMap(key);
            } else {
                return bind(key, clazz);
            }
        }
    }

    public static Class<?> getParameterizedClass(Type type) {
        if (type instanceof Class) {
            return (Class<?>) type;
        } else if (type instanceof WildcardType) {
            //<? extend > <? super >
            Type[] upperBounds = ((WildcardType) type).getUpperBounds();
            Type[] lowerBounds = ((WildcardType) type).getLowerBounds();
            if (upperBounds != null && upperBounds.length > 0) {
                Type upperBound = upperBounds[0];
                if (upperBound instanceof Class) {
                    return (Class<?>) upperBound;
                } else {
                    //TODO 没遇到过
                    throw new UnsupportedOperationException();
                }
            }
            if (lowerBounds != null && lowerBounds.length > 0) {
                Type lowerBound = lowerBounds[0];
                if (lowerBound instanceof Class) {
                    return (Class<?>) lowerBound;
                } else {
                    //TODO 没遇到过
                    throw new UnsupportedOperationException();
                }
            }
            //TODO 理论上不可能发生
            throw new UnsupportedOperationException();
        } else {
            //TODO TypeVariable
            throw new UnsupportedOperationException();
        }
    }

    /**
     * 获得一个属性List
     *
     * @param key key
     * @return 属性List
     */
    public List<Object> getList(String key) {
        return getList(key, Object.class);
    }

    /**
     * 获得一个属性List，类型为指定的Class
     *
     * @param key   key
     * @param clazz 值类型的class
     * @param <T>   泛型
     * @return 属性List
     */
    public abstract <T> List<T> getList(String key, Class<T> clazz);

    /**
     * 获得一个属性Set
     *
     * @param key key
     * @return 属性Set
     */
    public Set<Object> getSet(String key) {
        return getSet(key, Object.class);
    }

    /**
     * 获得一个属性Set，类型为指定的Class
     *
     * @param key   key
     * @param clazz 值类型的class
     * @param <T>   泛型
     * @return 属性Set
     */
    public abstract <T> Set<T> getSet(String key, Class<T> clazz);

    /**
     * 获得一个属性Map
     *
     * @param key key
     * @return 属性Map
     */
    public Map<String, Object> getMap(String key) {
        return getMap(key, Object.class);
    }

    /**
     * 获得一个属性Map，类型为指定的Class
     *
     * @param key   key
     * @param clazz 值类型的class
     * @param <T>   泛型
     * @return 属性Map
     */
    public abstract <T> Map<String, T> getMap(String key, Class<T> clazz);

    /**
     * 获得一个属性对象
     *
     * @param clazz 类型class
     * @param <T>   泛型
     * @return 自定义属性对象
     */
    public <T> T bind(Class<T> clazz) {
        return bind("", clazz);
    }

    /**
     * 获得一个属性对象
     *
     * @param key   key
     * @param clazz 类型class
     * @param <T>   泛型
     * @return 自定义属性对象
     */
    public abstract <T> T bind(String key, Class<T> clazz);

    /**
     * 绑定一个属性对象
     *
     * @param instance 需要绑定的属性对象
     * @param <T>      泛型
     * @return 绑定后的属性对象
     */
    public <T> T bind(T instance) {
        return bind("", instance);
    }

    /**
     * 绑定一个属性对象
     *
     * @param key      key
     * @param instance 需要绑定的属性对象
     * @param <T>      泛型
     * @return 绑定后的属性对象
     */
    public abstract <T> T bind(String key, T instance);
}
