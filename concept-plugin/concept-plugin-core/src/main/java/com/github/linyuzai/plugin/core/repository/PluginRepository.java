package com.github.linyuzai.plugin.core.repository;

import com.github.linyuzai.plugin.core.concept.Plugin;
import com.github.linyuzai.plugin.core.concept.PluginDefinition;

import java.util.stream.Stream;

/**
 * 插件仓储
 */
public interface PluginRepository {

    /**
     * 获得插件
     */
    Plugin get(PluginDefinition definition);

    /**
     * 添加插件
     */
    void add(Plugin plugin);

    /**
     * 移除插件
     */
    Plugin remove(PluginDefinition definition);

    /**
     * 包含插件
     */
    boolean contains(PluginDefinition definition);

    /**
     * 插件流
     */
    Stream<Plugin> stream();
}
