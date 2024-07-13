package com.github.linyuzai.plugin.core.autoload;

import com.github.linyuzai.plugin.core.event.PluginErrorEvent;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

/**
 * 插件自动卸载失败事件
 */
@Getter
@RequiredArgsConstructor
public class PluginAutoUnloadErrorEvent implements PluginAutoEvent, PluginErrorEvent {

    /**
     * 插件路径
     */
    private final String path;

    private final Throwable error;
}
