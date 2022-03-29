package com.github.linyuzai.plugin.core.concept;

/**
 * 插件概念
 */
public interface PluginConcept {

    /**
     * 加载插件
     *
     * @param o 插件源
     * @return 插件 {@link Plugin}
     */
    Plugin load(Object o);
}
