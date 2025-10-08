package com.github.linyuzai.plugin.core.concept;

import com.github.linyuzai.plugin.core.sync.SyncSupport;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public abstract class AbstractPluginEntry extends SyncSupport implements Plugin.Entry {

    private final Plugin parent;

    private final String name;

    @Override
    public long getCreateTime() {
        return parent.getDefinition().getCreateTime();
    }

    @Override
    public Object getVersion() {
        return parent.getDefinition().getVersion();
    }
}
