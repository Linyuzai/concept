package com.github.linyuzai.plugin.core.event;

import com.github.linyuzai.plugin.core.concept.Plugin;
import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * {@link PluginEvent} 抽象类，附带插件 {@link Plugin} 对象
 */
@Getter
@AllArgsConstructor
public abstract class AbstractPluginEvent implements PluginEvent {

    /**
     * 插件
     */
    private final Plugin plugin;
}
