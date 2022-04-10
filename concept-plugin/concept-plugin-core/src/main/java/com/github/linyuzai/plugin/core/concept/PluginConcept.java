package com.github.linyuzai.plugin.core.concept;

import java.util.Map;

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
     * @return 插件 {@link Plugin}
     */
    Plugin load(Object o);

    /**
     * 卸载插件
     *
     * @param o 插件源
     */
    Plugin unload(Object o);

    boolean isLoad(Object o);

    /**
     * 发布事件
     *
     * @param event 事件
     */
    void publish(Object event);

    /**
     * 获得插件
     *
     * @param id 插件 id
     * @return 插件或 null
     */
    Plugin getPlugin(Object id);

    /**
     * 获得所有的加载的插件
     *
     * @return 所有的加载的插件
     */
    Map<Object, Plugin> getPlugins();
}
