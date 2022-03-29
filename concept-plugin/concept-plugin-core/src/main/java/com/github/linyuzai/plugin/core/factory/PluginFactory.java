package com.github.linyuzai.plugin.core.factory;

import com.github.linyuzai.plugin.core.concept.Plugin;
import com.github.linyuzai.plugin.core.concept.PluginConcept;

/**
 * 插件工厂
 */
public interface PluginFactory {

    /**
     * 是否支持插件创建
     *
     * @param o       插件源
     * @param concept {@link PluginConcept}
     * @return 如果支持返回 true，否则返回 false
     */
    boolean support(Object o, PluginConcept concept);

    /**
     * 创建插件 {@link Plugin}
     *
     * @param o       插件源
     * @param concept {@link PluginConcept}
     * @return 插件 {@link Plugin}
     */
    Plugin create(Object o, PluginConcept concept);
}
