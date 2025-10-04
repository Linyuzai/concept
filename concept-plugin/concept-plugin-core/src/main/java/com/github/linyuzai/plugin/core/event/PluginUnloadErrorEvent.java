package com.github.linyuzai.plugin.core.event;

import com.github.linyuzai.plugin.core.concept.PluginDefinition;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

/**
 * 插件卸载事件
 */
@Getter
@RequiredArgsConstructor
public class PluginUnloadErrorEvent implements PluginErrorEvent {

    private final PluginDefinition definition;

    private final Throwable error;
}
