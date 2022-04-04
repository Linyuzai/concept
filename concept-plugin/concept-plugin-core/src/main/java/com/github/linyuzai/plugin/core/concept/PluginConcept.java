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
     * @param plugin 插件
     */
    void load(Plugin plugin);

    /**
     * 创建并加载插件
     *
     * @param o 插件源
     */
    default void load(Object o) {
        Plugin plugin = create(o);
        load(plugin);
    }
}
