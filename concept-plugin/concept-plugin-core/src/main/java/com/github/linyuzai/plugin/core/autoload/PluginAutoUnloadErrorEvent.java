package com.github.linyuzai.plugin.core.autoload;

import com.github.linyuzai.plugin.core.event.PluginErrorEvent;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

/**
 * 插件自动加载事件
 */
@Getter
@RequiredArgsConstructor
public class PluginAutoUnloadErrorEvent implements PluginAutoEvent, PluginErrorEvent {

    private final String path;

    private final Throwable error;
}
