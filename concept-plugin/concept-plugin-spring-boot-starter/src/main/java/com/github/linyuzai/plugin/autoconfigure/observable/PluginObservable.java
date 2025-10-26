package com.github.linyuzai.plugin.autoconfigure.observable;

import java.util.Collection;
import java.util.List;
import java.util.function.BiConsumer;

/**
 * 插件被观察者
 */
public interface PluginObservable<K, V> {

    /**
     * 获得 keys
     */
    Collection<K> keys();

    /**
     * 获得插件
     */
    Collection<V> values();

    /**
     * 获得插件
     */
    V get(K key);

    /**
     * 获得插件列表
     */
    List<V> list(K key);

    /**
     * 遍历插件
     */
    void forEach(BiConsumer<? super K, ? super V> action);
}
