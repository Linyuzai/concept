package com.github.linyuzai.plugin.core.event;

import com.github.linyuzai.plugin.core.concept.Plugin;
import com.github.linyuzai.plugin.core.concept.PluginDefinition;

/**
 * 插件事件
 */
public interface PluginEvent extends PluginDefinitionEvent {

    /**
     * 获得插件
     */
    Plugin getPlugin();

    @Override
    default PluginDefinition getDefinition() {
        return getPlugin().getDefinition();
    }
}
