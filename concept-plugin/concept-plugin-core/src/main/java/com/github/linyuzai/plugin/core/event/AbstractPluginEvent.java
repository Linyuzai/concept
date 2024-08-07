package com.github.linyuzai.plugin.core.event;

import com.github.linyuzai.plugin.core.concept.Plugin;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

/**
 * 插件事件抽象类，附带插件对象
 */
@Getter
@RequiredArgsConstructor
public abstract class AbstractPluginEvent implements PluginEvent {

    /**
     * 插件
     */
    private final Plugin plugin;
}
