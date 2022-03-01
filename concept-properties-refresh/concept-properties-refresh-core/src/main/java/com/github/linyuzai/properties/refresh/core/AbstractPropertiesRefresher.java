package com.github.linyuzai.properties.refresh.core;

import com.github.linyuzai.properties.refresh.core.condition.RefreshPropertiesCondition;
import lombok.AllArgsConstructor;

import java.lang.ref.WeakReference;
import java.lang.reflect.*;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * 刷新器的抽象实现
 */
@AllArgsConstructor
public abstract class AbstractPropertiesRefresher implements PropertiesRefresher {

    protected final WeakReference<Object> target;

    public AbstractPropertiesRefresher(Object target) {
        this.target = new WeakReference<>(target);
    }

    /**
     * 是否需要刷新
     *
     * @param condition 刷新条件
     * @return 是否需要刷新
     */

    public abstract void doRefresh(RefreshPropertiesCondition condition);

    @Override
    public void refresh(PlatformProperties properties) {


    }

    /**
     * 通过给定的类型，匹配的key，和配置属性，获得对应的值
     *
     * @param metadata   给定的类型和匹配的key
     * @param properties 配置属性源
     * @return 属性值
     */
    public static Object getValue(KeyTypePair pair, PlatformProperties properties) {
        String key = pair.getKey();
        Type type = pair.getType();
        if (type instanceof ParameterizedType) {
            //如果是普通的泛型
            Type[] arguments = ((ParameterizedType) type).getActualTypeArguments();
            Type rawType = ((ParameterizedType) type).getRawType();
            if (rawType instanceof Class) {
                //主Class
                Class<?> clazz = (Class<?>) rawType;
                if (List.class.isAssignableFrom(clazz)) {
                    return properties.getList(key, getParameterizedClass(arguments[0]));
                } else if (Set.class.isAssignableFrom(clazz)) {
                    return properties.getSet(key, getParameterizedClass(arguments[0]));
                } else if (Map.class.isAssignableFrom(clazz)) {
                    return properties.getMap(key, getParameterizedClass(arguments[1]));
                } else {
                    //TODO 自定义的泛型类？
                    return properties.bind(key, clazz);
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
                return properties.getList(key);
            } else if (Set.class.isAssignableFrom(clazz)) {
                return properties.getSet(key);
            } else if (Map.class.isAssignableFrom(clazz)) {
                return properties.getMap(key);
            } else {
                return properties.bind(key, clazz);
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
}
