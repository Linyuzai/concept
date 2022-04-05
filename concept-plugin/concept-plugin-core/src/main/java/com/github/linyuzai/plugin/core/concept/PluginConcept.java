package com.github.linyuzai.plugin.core.concept;

/**
 * 插件概念
 */
public interface PluginConcept {

    /**
     * 创建插件
     *
     * @param o 插件源
     * @return 插件 {@link Plugin}
     */
    Plugin create(Object o);

    /**
     * 加载插件
     *
     * @param o 插件源
     */
    void load(Object o);
}
