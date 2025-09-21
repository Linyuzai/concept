package com.github.linyuzai.plugin.core.tree;

import com.github.linyuzai.plugin.core.concept.PluginDefinition;

import java.util.stream.Stream;

public interface PluginTreeRepository {

    /**
     * 获得插件树
     */
    PluginTree get(PluginDefinition definition);

    /**
     * 添加插件
     */
    void add(PluginTree tree);

    /**
     * 移除插件
     */
    PluginTree remove(PluginDefinition definition);

    /**
     * 包含插件
     */
    boolean contains(PluginDefinition definition);

    /**
     * 插件流
     */
    Stream<PluginTree> stream();
}
