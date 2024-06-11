package com.github.linyuzai.plugin.core.event;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public class PluginLoadErrorEvent implements PluginErrorEvent {

    private final Throwable error;
}
