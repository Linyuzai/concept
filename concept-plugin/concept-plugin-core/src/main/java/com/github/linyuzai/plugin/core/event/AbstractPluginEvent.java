package com.github.linyuzai.plugin.core.event;

import com.github.linyuzai.plugin.core.concept.Plugin;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public abstract class AbstractPluginEvent implements PluginEvent {

    private final Plugin plugin;
}
