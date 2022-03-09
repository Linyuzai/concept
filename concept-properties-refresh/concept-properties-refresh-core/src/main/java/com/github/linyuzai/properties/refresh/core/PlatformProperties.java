package com.github.linyuzai.properties.refresh.core;

import com.github.linyuzai.properties.refresh.core.condition.RefreshPropertiesCondition;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.lang.reflect.Type;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * 配置属性
 * 支持动态更新
 */
public interface PlatformProperties {

    /**
     * 获得一个属性List
     *
     * @param key key
     * @return 属性List
     */
    default List<Object> getList(String key) {
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
    <T> List<T> getList(String key, Class<T> clazz);

    /**
     * 获得一个属性Set
     *
     * @param key key
     * @return 属性Set
     */
    default Set<Object> getSet(String key) {
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
    <T> Set<T> getSet(String key, Class<T> clazz);

    /**
     * 获得一个属性Map
     *
     * @param key key
     * @return 属性Map
     */
    default Map<String, Object> getMap(String key) {
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
    <T> Map<String, T> getMap(String key, Class<T> clazz);

    /**
     * 获得一个属性对象
     *
     * @param clazz 类型class
     * @param <T>   泛型
     * @return 自定义属性对象
     */
    default <T> T bind(Class<T> clazz) {
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
    <T> T bind(String key, Class<T> clazz);

    /**
     * 绑定一个属性对象
     *
     * @param instance 需要绑定的属性对象
     * @param <T>      泛型
     * @return 绑定后的属性对象
     */
    default <T> T bind(T instance) {
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
    <T> T bind(String key, T instance);

    /**
     * 获得所有的属性
     *
     * @return 所有的属性
     */
    Map<String, Object> all();

    /**
     * 属性注册
     * 解析注解
     *
     * @param target 被处理的对象
     */
    default void register(Object target) {

    }

    /**
     * 刷新
     */
    default void refresh() {

    }

    /**
     * 根据刷新条件刷新
     *
     * @param condition 刷新条件
     */
    default void refresh(RefreshPropertiesCondition condition) {

    }

    /**
     * 属性元数据
     */
    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    class Metadata {

        /**
         * 匹配的key
         */
        private String key;

        /**
         * 属性的类型
         */
        private Type type;
    }
}
