package com.github.linyuzai.plugin.core.concept;

import com.github.linyuzai.plugin.core.context.PluginContext;
import com.github.linyuzai.plugin.core.extract.PluginExtractor;

import java.util.Collection;
import java.util.Map;

/**
 * 插件概念
 */
public interface PluginConcept {

    void initialize();

    void destroy();

    void addExtractors(PluginExtractor... extractors);

    void addExtractors(Collection<? extends PluginExtractor> extractors);

    void removeExtractors(PluginExtractor... extractors);

    void removeExtractors(Collection<? extends PluginExtractor> extractors);

    /**
     * 创建插件
     *
     * @param o 插件源
     * @return 插件 {@link Plugin}
     */
    Plugin create(Object o, PluginContext context);

    /**
     * 加载插件
     *
     * @param o 插件源
     * @return 插件 {@link Plugin}
     */
    Plugin load(Object o);

    /**
     * 加载插件
     *
     * @param o 插件源
     * @return 插件 {@link Plugin}
     */
    Plugin load(Object o, String group);

    Plugin unload(Object o);

    /**
     * 卸载插件
     *
     * @param o 插件源
     */
    Plugin unload(Object o, String group);

    boolean isLoaded(Object o);

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
