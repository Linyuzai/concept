package com.github.linyuzai.plugin.core.autoload;

import com.github.linyuzai.plugin.core.concept.PluginDefinition;
import com.github.linyuzai.plugin.core.event.PluginErrorEvent;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

/**
 * 插件自动加载失败事件
 */
@Getter
@RequiredArgsConstructor
public class PluginAutoLoadErrorEvent implements PluginAutoEvent, PluginErrorEvent {

    private final PluginDefinition definition;

    private final Throwable error;
}
