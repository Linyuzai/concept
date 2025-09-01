package com.github.linyuzai.plugin.core.concept;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public abstract class AbstractPluginEntry implements Plugin.Entry {

    private final String name;

    private final Plugin plugin;

    @Override
    public long getCreateTime() {
        return plugin.getDefinition().getCreateTime();
    }

    @Override
    public Object getVersion() {
        return plugin.getDefinition().getVersion();
    }

    @Override
    public Object getSource() {
        return this;
    }
}
