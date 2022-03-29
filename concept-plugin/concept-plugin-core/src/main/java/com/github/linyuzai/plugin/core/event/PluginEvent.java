package com.github.linyuzai.plugin.core.event;

import com.github.linyuzai.plugin.core.concept.Plugin;

/**
 * 插件事件
 */
public interface PluginEvent {

    /**
     * 获得插件 {@link Plugin}
     *
     * @return {@link Plugin}
     */
    Plugin getPlugin();
}
