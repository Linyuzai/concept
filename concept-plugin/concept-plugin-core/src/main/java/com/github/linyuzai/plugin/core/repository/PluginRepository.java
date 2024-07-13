package com.github.linyuzai.plugin.core.repository;

import com.github.linyuzai.plugin.core.concept.Plugin;

import java.util.stream.Stream;

/**
 * 插件仓储
 */
public interface PluginRepository {

    /**
     * 获得插件
     */
    Plugin get(Object o);

    /**
     * 添加插件
     */
    void add(Plugin plugin);

    /**
     * 移除插件
     */
    Plugin remove(Object o);

    /**
     * 包含插件
     */
    boolean contains(Object o);

    /**
     * 插件流
     */
    Stream<Plugin> stream();
}
