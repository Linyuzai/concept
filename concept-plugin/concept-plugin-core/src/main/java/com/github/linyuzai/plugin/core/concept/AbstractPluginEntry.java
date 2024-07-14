package com.github.linyuzai.plugin.core.concept;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public abstract class AbstractPluginEntry implements Plugin.Entry {

    private final String name;

    private final Plugin plugin;
}
