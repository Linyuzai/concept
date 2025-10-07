package com.github.linyuzai.plugin.core.factory;

import com.github.linyuzai.plugin.core.metadata.PluginMetadata;
import com.github.linyuzai.plugin.core.metadata.PluginMetadataFactory;
import lombok.Getter;

import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

@Getter
public abstract class AbstractPluginFactory implements PluginFactory, PluginMetadataFactory {

    private final List<PluginMetadata.Adapter> metadataAdapters = new CopyOnWriteArrayList<>();

    protected PluginMetadata.Adapter getMetadataAdapter(String name) {
        for (PluginMetadata.Adapter adapter : metadataAdapters) {
            if (adapter.support(name)) {
                return adapter;
            }
        }
        return null;
    }
}
